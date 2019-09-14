package firis.yuzukitools.common.world.dimension.skygarden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import firis.core.common.helper.BlockPosHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * 空中庭園の管理クラス
 * @author computer
 *
 */
public class SkyGardenManager {

	private static SkyGardenManager instance = null;
	
	/**
	 * 0,0地点から浮島の距離
	 */
	public static int RADIUS_FLOATING_ISLAND = 500;
	
	
	/**
	 * 浮島の生成座標
	 */
	public static int FLOATING_ISLAND_Y = 128;
	
	/**
	 * 
	 * @return
	 */
	public static SkyGardenManager getInstance() {
		if (instance == null) {
			instance = new SkyGardenManager();
		}
		return instance;
	}
	
	
	private final Map<String, List<BlockPos>> chunkPosMap;
	
	private final List<Vec3i> chunkCoordList;
	
	
	/**
	 * Singletonコンストラクタ
	 */
	private SkyGardenManager() {
		
		//浮島の構造体を生成する
		this.chunkPosMap = this.initFloatingIsland();
		
		//浮島の位置を計算する
		this.chunkCoordList = this.initFloatingIslandCoord();
	}
	
	
	/**
	 * 3×3Chunkの浮島定義を生成する
	 */
	private Map<String, List<BlockPos>> initFloatingIsland() {
		
		List<BlockPos> posList = BlockPosHelper.getBlockPosHemisphere(24, 1.0D / 1.5D, true);
		//3chunk分に分割しないといけない
		Map<String, List<BlockPos>> retChunkPosMap = new HashMap<>();
		for (BlockPos base : posList) {
			//端を基準点にする
			BlockPos pos = base.add(24, 0, 24);
			
			//chunkの範囲を生成する
			//0から2までの範囲
			Integer chunkX = (int) Math.ceil((double)(pos.getX() + 1) / 16.0D) - 1;
			Integer chunkZ = (int) Math.ceil((double)(pos.getZ() + 1) / 16.0D) - 1;
			String chunkKey = chunkX.toString() + "_" + chunkZ.toString();
			
			List<BlockPos> list;
			if (retChunkPosMap.containsKey(chunkKey)) {
				list = retChunkPosMap.get(chunkKey);
			} else {
				list = new ArrayList<>();
			}
			
			//chunkにあわせて基準点をさらにずらす
			pos = pos.add(-(chunkX * 16), 0, -(chunkZ * 16));
			list.add(pos);
			
			retChunkPosMap.put(chunkKey, list);
		}
		return retChunkPosMap;
	}
	
	/**
	 * 0,0 Chunkからの1000Chunk離れた位置を計算する
	 * 16個分
	 */
	private List<Vec3i> initFloatingIslandCoord() {
		
		List<Vec3i> chunkList = new ArrayList<>();
		
		int radius = RADIUS_FLOATING_ISLAND;
		
		for (int i = 0; i < 16; i++) {
			//角度
			double radian = Math.toRadians(i * 22.5);
			
			double x = radius * Math.cos(radian);
			double z = radius * Math.sin(radian);
			
			x = Math.round(x);
			z = Math.round(z);
			
			//Chunk座標を計算して設定
			chunkList.add(new Vec3i(x, 0, z));
			
		}
		return chunkList;
	}
	
	/**
	 * 該当チャンクが浮島の生成場所かのチェックを行う
	 * @param x
	 * @param z
	 */
	private String checkFloatingIsland(int x, int z) {
		
		String chunkKey = "";
		
		for (Vec3i vec : chunkCoordList) {
			
			int chunkX = vec.getX();
			int chunkZ = vec.getZ();
			
			Integer flgX = -1;
			Integer flgZ = -1;
			
			//X判定
			if (chunkX - 1 <= x && x <= chunkX + 1) {
				flgX = x - chunkX + 1;
			}
			//Z判定
			if (chunkZ - 1 <= z && z <= chunkZ + 1) {
				flgZ = z - chunkZ + 1;
			}
			
			if (flgX >= 0 && flgZ >= 0) {
				chunkKey = flgX.toString() + "_" + flgZ.toString();
				break;
			}
			
		}
		
		return chunkKey;
	}
	
	/**
	 * 対象チャンクの浮島の構造リストを返却する
	 * @param x
	 * @param z
	 * @return
	 */
	@Nullable
	public List<BlockPos> getFloatingIslandBlockPosList(int x, int z) {
		
		String chunkKey = checkFloatingIsland(x, z);
		if ("".equals(chunkKey)) {
			return null;
		}
		
		//生成高度
		return this.chunkPosMap.get(chunkKey);
	}
	
	
	/**
	 * 色に応じたスポーンポイントを生成する
	 * @return
	 */
	public BlockPos getSpawnPoint(int meta) {
		int idx = Math.min(meta, 15);
		idx = Math.max(meta, 0);
		
		Vec3i chunk = this.chunkCoordList.get(idx);
		
		return new BlockPos(chunk.getX() * 16, FLOATING_ISLAND_Y + 1, chunk.getZ() * 16);
	}
	
}
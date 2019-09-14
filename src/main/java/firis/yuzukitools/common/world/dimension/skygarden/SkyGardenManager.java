package firis.yuzukitools.common.world.dimension.skygarden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import firis.core.common.helper.BlockPosHelper;
import firis.yuzukitools.common.world.dimension.DimensionHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

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
	 * プレイヤーの戻り先を保存する
	 */
	private static Map<UUID, TeleporterData> returnTeleporterMap = new HashMap<>(); 
	
	/**
	 * テレポートの情報をNBTから復元する
	 */
	public static void readFromNBT(NBTTagCompound nbt) {
	
		//データが既に存在している場合は何もしない
		if (returnTeleporterMap.size() > 0) return;
		
		//設定データなし
		if (nbt == null || !nbt.hasKey("teleportdata")) return;

		//初期化
		returnTeleporterMap = new HashMap<>();

		NBTTagList list = nbt.getTagList("teleportdata", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) list.get(i);
			UUID uuid = tag.getUniqueId("uuid");
			int dimensionId = tag.getInteger("dimensionId");
			double posX = tag.getDouble("posX");
			double posY = tag.getDouble("posY");
			double posZ = tag.getDouble("posZ");
			
			//データを設定
			returnTeleporterMap.put(uuid, new TeleporterData(dimensionId,
					posX,
					posY,
					posZ));
		}		
	}
	
	/**
	 * テレポートの情報をNBTから復元する
	 */
	public static NBTTagCompound writeToNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList list = new NBTTagList();
		for (UUID uuid : returnTeleporterMap.keySet()) {
			TeleporterData teleData = returnTeleporterMap.get(uuid);
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("dimensionId", teleData.dimensionId);
			data.setDouble("posX", teleData.posX);
			data.setDouble("posY", teleData.posY);
			data.setDouble("posZ", teleData.posZ);
			data.setUniqueId("uuid", uuid);
			list.appendTag(data);
		}
		nbt.setTag("teleportdata", list);
		
		return nbt;
	}

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
	
	/**
	 * 空中庭園のテレポート処理
	 */
	public void TeleporterSkyGarden(EntityPlayer player, int meta) {
		
		if (player == null) return;
		World world = player.getEntityWorld();
		
		//現在のディメンションを判断する
		boolean isSkyGarden = false;
		boolean isFloatingIsland = false;
		if (world.provider.getDimension() == DimensionHandler.dimensionSkyGarden.getId()) {
			isSkyGarden = true;
			
			//SkyGardenの場合は現在座標が浮島範囲内か確認する
			Vec3i chunk = this.chunkCoordList.get(meta);
			
			Chunk playerChunk = world.getChunkFromBlockCoords(player.getPosition());
			
			//3chunk以内
			if (chunk.getX() - 1 <= playerChunk.x && playerChunk.x <= chunk.getX() + 1
					&& chunk.getZ() - 1 <= playerChunk.z && playerChunk.z <= chunk.getZ() + 1) {
				isFloatingIsland = true;
			}
			
		}
		
		if (!isSkyGarden) {
			
			//テレポート処理はServerサイドのみ
			if(world.isRemote) return;
			
			//テレポート先を保存
			setReturnTeleporterData(player);
			
			//別ディメンションからテレポートする
			WorldServer server;
			server = player.getServer().getWorld(DimensionHandler.dimensionSkyGarden.getId());
			
			//スポーン位置を設定
			server.setSpawnPoint(SkyGardenManager.getInstance().getSpawnPoint(meta));
			
			player.changeDimension(DimensionHandler.dimensionSkyGarden.getId(), new TeleporterSkyGarden(server, meta));
			
		} else if (isFloatingIsland){
			//元の世界へ戻る
			//テレポート処理はServerサイドのみ
			if(world.isRemote) return;
			
			TeleporterData teledata = getReturnTeleporterData(player);
			//別ディメンションからテレポートする
			WorldServer server;
			server = player.getServer().getWorld(teledata.dimensionId);
			player.changeDimension(teledata.dimensionId, 
					new TeleporterDimension(server, teledata.posX, teledata.posY, teledata.posZ));
			
		} else {
		
			//同一ディメンションから移動
			BlockPos pos = getSpawnPoint(meta);
			
			player.setLocationAndAngles((double)pos.getX() + 0.5, 
	        		(double)pos.getY(), 
	        		(double)pos.getZ() + 0.5, 0, 0.0F);
			
		}
		
	}
	
	/**
	 * テレポートの戻り先を設定する
	 * @param plyaer
	 */
	protected void setReturnTeleporterData(EntityPlayer player) {
		//テレポートの戻り先を保存する
		returnTeleporterMap.put(player.getUniqueID(), new TeleporterData(
				player.getEntityWorld().provider.getDimension(),
				player.posX,
				player.posY,
				player.posZ));
	}
	
	/**
	 * テレポートの戻り先を取得する
	 * @param player
	 * @return
	 */
	protected TeleporterData getReturnTeleporterData(EntityPlayer player) {
		
		if (returnTeleporterMap.containsKey(player.getUniqueID())) {
			return returnTeleporterMap.get(player.getUniqueID());
		}
		
		//対象がない場合はオーバーワールドにランダムスポーン
		WorldServer server;
		server = player.getServer().getWorld(DimensionType.OVERWORLD.getId());
		BlockPos pos = server.provider.getRandomizedSpawnPoint();
		return new TeleporterData(DimensionType.OVERWORLD.getId(), 
				pos.getX() + 0.5D, 
				pos.getY() + 0.0D, 
				pos.getZ() + 0.5D);
	}
	
	/**
	 * テレポーター戻り座標保存用クラス
	 */
	protected static class TeleporterData {
		
		public int dimensionId = 0;
		public double posX = 0;
		public double posY = 0;
		public double posZ = 0;
		
		public TeleporterData(int dimensionId, double posX, double posY, double posZ) {
			this.dimensionId = dimensionId;
			this.posX = posX;
			this.posY = posY;
			this.posZ = posZ;
		}
		
	}
}

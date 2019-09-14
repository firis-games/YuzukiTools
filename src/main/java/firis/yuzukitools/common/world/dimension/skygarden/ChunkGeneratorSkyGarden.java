package firis.yuzukitools.common.world.dimension.skygarden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firis.core.common.helper.BlockPosHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * ChunkGeneratorSkyGarden
 * 空中庭園のチャンク生成処理
 * @author computer
 *
 */
public class ChunkGeneratorSkyGarden implements IChunkGenerator {

	private final World world;
	
	private final Map<String, List<BlockPos>> chunkPosMap;
	
	/**
	 * コンストラクタ
	 * @param world
	 */
	public ChunkGeneratorSkyGarden(World world) {
		this.world = world;
		this.chunkPosMap = initFloatingIsland();
	}
	
	/**
	 * チャンクを生成する
	 */
	@Override
	public Chunk generateChunk(int x, int z) {
		
		Chunk chunk = new Chunk(this.world, x, z);
		
		//浮島の生成
		this.createFloatingIsland(chunk);
		
		//光源チェック
		chunk.checkLight();
		
		return chunk;
	}

	@Override
	public void populate(int x, int z) {		
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return this.world.getBiome(pos).getSpawnableList(creatureType);
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
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
	 * 生成テスト
	 * @param chunk
	 * @return
	 */
	private void createFloatingIsland(Chunk chunk) {
		
		Integer chunkX = -1;
		Integer chunkZ = -1;
		
		//chunkの範囲が-1から1の合計9chunk
		if (chunk.x >= -1 && chunk.x <= 1) {
			chunkX = chunk.x + 1;
		}
		if (chunk.z >= -1 && chunk.z <= 1) {
			chunkZ = chunk.z + 1;
		}
		
		//対象外の場合は何もしない
		if (chunkX == -1 || chunkZ == -1) {
			return;
		}
		String chunkKey = chunkX.toString() + "_" + chunkZ.toString();
		
		//生成高度
		BlockPos base = new BlockPos(0, 80, 0);
		
		List<BlockPos> posList = this.chunkPosMap.get(chunkKey);
		for (BlockPos pos : posList) {
			if (pos.getY() == 0) {
				chunk.setBlockState(pos.add(base), Blocks.GRASS.getDefaultState());
			} else {
				chunk.setBlockState(pos.add(base), Blocks.DIRT.getDefaultState());
			}
		}
	}

}

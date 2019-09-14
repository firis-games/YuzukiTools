package firis.yuzukitools.common.world.dimension.skygarden;

import java.util.List;

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
	
	/**
	 * コンストラクタ
	 * @param world
	 */
	public ChunkGeneratorSkyGarden(World world) {
		this.world = world;
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
	 * 浮島の生成処理
	 * @param chunk
	 * @return
	 */
	private void createFloatingIsland(Chunk chunk) {
		
		
		List<BlockPos> posList = SkyGardenManager
				.getInstance()
				.getFloatingIslandBlockPosList(chunk.x, chunk.z);
		
		if (posList == null) return;
		
		//生成高度
		BlockPos base = new BlockPos(0, SkyGardenManager.FLOATING_ISLAND_Y, 0);
				
		for (BlockPos pos : posList) {
			if (pos.getY() == 0) {
				chunk.setBlockState(pos.add(base), Blocks.GRASS.getDefaultState());
			} else {
				chunk.setBlockState(pos.add(base), Blocks.DIRT.getDefaultState());
			}
		}
	}

}

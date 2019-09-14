package firis.yuzukitools.common.world.dimension.skygarden;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.world.dimension.DimensionHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * 空中庭園provider
 * @author computer
 *
 */
public class WorldProviderSkyGarden extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return DimensionHandler.dimensionSkyGarden;
	}

	@Override
    protected void init() {
        
		this.doesWaterVaporize = false;
		this.hasSkyLight = true;

		Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(YuzukiTools.MODID, "sky_garden"));
		this.biomeProvider = new BiomeProviderSingle(biome);
    }
	
	@Override
    public IChunkGenerator createChunkGenerator()
    {
		return new ChunkGeneratorSkyGarden(this.world);
    }
	
	/**
	 * スポーン位置を設定する
	 */
	public BlockPos getRandomizedSpawnPoint()
    {
		//浮島の位置へスポーンさせる
        return SkyGardenManager.getInstance().getSpawnPoint(0);
    }
}

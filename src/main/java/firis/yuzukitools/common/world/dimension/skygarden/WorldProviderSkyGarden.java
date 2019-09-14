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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	 * ランダムスポーンの位置
	 */
	public BlockPos getRandomizedSpawnPoint()
    {
		//ランダムスポーンの位置
        return SkyGardenManager.getInstance().getSpawnPoint(this.world.rand.nextInt(16));
    }
	
	/**
	 * 雲の高さ
	 */
	@Override
	@SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return 10.0f;
    }
	
}

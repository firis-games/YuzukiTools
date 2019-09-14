package firis.yuzukitools.common.world.dimension.skygarden.biome;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeSkyGarden extends Biome {

	/**
	 * コンストラクタ
	 */
	public BiomeSkyGarden() {
		
		super(new BiomeProperties("SkyGarden"));
		this.setRegistryName(new ResourceLocation(YuzukiTools.MODID, "sky_garden"));

		//スポーン対象再設定
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
	}

}

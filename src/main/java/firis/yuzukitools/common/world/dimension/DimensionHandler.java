package firis.yuzukitools.common.world.dimension;

import firis.yuzukitools.common.world.dimension.skygarden.WorldProviderSkyGarden;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/**
 * ディメンション管理
 * @author computer
 *
 */
public class DimensionHandler {
	
	/**
	 * 空中庭園ディメンション
	 */
	public static DimensionType dimensionSkyGarden;
	
	/**
	 * dimension初期化
	 */
	public static void init() {
		
		//アルフヘイムDimensionを登録
		dimensionSkyGarden = DimensionType.register(
				"Sky Garden Dimension", 
				"_sky_garden", 
				DimensionManager.getNextFreeDimId(), 
				WorldProviderSkyGarden.class, false);
		
		DimensionManager.registerDimension(dimensionSkyGarden.getId(), dimensionSkyGarden);
	}
}

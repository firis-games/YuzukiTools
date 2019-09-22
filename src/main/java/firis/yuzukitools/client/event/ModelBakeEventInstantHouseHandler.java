package firis.yuzukitools.client.event;

import firis.yuzukitools.client.item.IBakedModelInstantHouse;
import firis.yuzukitools.client.item.ItemOverrideListInstantHouse;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBakeEventInstantHouseHandler {

	private static final IBakedModel model = new IBakedModelInstantHouse();
	
	/**
	 * Inventoryアイコン描画用Event
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		
		//デフォルトモデルを退避
		ModelResourceLocation baseModelLoc = ItemOverrideListInstantHouse.modelLocation;
		ModelResourceLocation defModelLoc = ItemOverrideListInstantHouse.defModelLocation;
		
		IBakedModel baseModel = event.getModelRegistry().getObject(baseModelLoc);

		//ベースをカスタムモデルへ差し替え
		event.getModelRegistry().putObject(baseModelLoc, model);
		
		//デフォルトモデルを別IDで登録
		event.getModelRegistry().putObject(defModelLoc, baseModel);
		
	}
	
}

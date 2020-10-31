package firis.yuzukitools.mobbottle.client.event;

import firis.mobbottle.client.model.BakedModelMobBottle;
import firis.mobbottle.client.teisr.FTileMobBottleItemStackRenderer;
import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class YKMobBottlePlusModelBakeEventHandler {

	/**
	 * Item描画でTESRを利用する設定
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		
		//BakedModel取得
		ModelResourceLocation mrlMobBottle = new ModelResourceLocation(YuzukiTools.MODID + ":mob_bottle_plus", "inventory");
		IBakedModel modelMobBottle = event.getModelRegistry().getObject(mrlMobBottle);
		
		//BakedModelMobBottleインスタンスを保持
		FTileMobBottleItemStackRenderer.bakedModelMobBottleInstance = new BakedModelMobBottle(modelMobBottle);
		
		//BakedModelの差し替え
		event.getModelRegistry().putObject(mrlMobBottle, FTileMobBottleItemStackRenderer.bakedModelMobBottleInstance);
		
	}
}

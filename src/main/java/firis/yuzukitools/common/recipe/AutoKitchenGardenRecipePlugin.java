package firis.yuzukitools.common.recipe;

import java.util.Iterator;

import firis.yuzukitools.api.recipe.IYuzukiToolsPlugin;
import firis.yuzukitools.api.recipe.KitchenGardenRegistry;
import firis.yuzukitools.api.recipe.YuzukiToolsPlugin;
import firis.yuzukitools.common.config.YKConfig;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

@YuzukiToolsPlugin(priority = EventPriority.LOW)
public class AutoKitchenGardenRecipePlugin implements IYuzukiToolsPlugin {

	/**
	 * 家庭菜園レシピ登録
	 * IPlantable アイテムを解析して登録を行う
	 */
	@Override
	public void registerKitchenGardenRecipe(KitchenGardenRegistry recipe) {

		//自動連携がオフの場合は何もしない
		if (YKConfig.USE_KITCHEN_GARDEN_AUTO_REGISTER != true) return;
		
		int progress = 300;
		
		Iterator<ResourceLocation> itemKeys = Item.REGISTRY.getKeys().iterator();
    	while (itemKeys.hasNext()) {
    		ResourceLocation rl = itemKeys.next();
    		Item item = Item.REGISTRY.getObject(rl);
    		//バニラ以外の種系アイテム
    		if (!"minecraft".equals(rl.getResourceDomain())
    				&& item instanceof IPlantable) {
    			recipe.registerIPlantable(item, progress);
    		}
    	}
	}
}

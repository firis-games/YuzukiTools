package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import firis.yuzukitools.api.recipe.IYuzukiToolsPlugin;
import firis.yuzukitools.api.recipe.KitchenGardenRegistry;
import firis.yuzukitools.api.recipe.YuzukiToolsPlugin;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

/**
 * ゆづきツールズのレシピ登録管理クラス
 * @author computer
 *
 */
public class RecipeManager {
	
	/**
	 * preInitで処理を行う
	 */
	public static void registerPlugin(ASMDataTable asmDataTable) {
		
    	//家庭菜園
    	Set<ASMData> asmDatas = asmDataTable.getAll(YuzukiToolsPlugin.class.getCanonicalName());
    	
    	List<IYuzukiToolsPlugin> pluginList = new ArrayList<>();
    	
    	//対象を取得する
    	for (ASMData asmData : asmDatas) {
    		try {
    			Class<?> asmClass = Class.forName(asmData.getClassName());
    			Class<? extends IYuzukiToolsPlugin> asmInstanceClass = asmClass.asSubclass(IYuzukiToolsPlugin.class);
    			pluginList.add(asmInstanceClass.newInstance());
    		} catch (Exception e) {
    		}
    	}
    	
    	KitchenGardenRegistry register = new KitchenGardenRegistry();
    	//レシピ登録処理
    	for (IYuzukiToolsPlugin plugin : pluginList) {
    		plugin.registerKitchenGardenRecipe(register);
    	}
	}

}

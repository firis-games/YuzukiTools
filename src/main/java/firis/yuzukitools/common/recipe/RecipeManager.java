package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.api.recipe.IYuzukiToolsPlugin;
import firis.yuzukitools.api.recipe.KitchenGardenRegistry;
import firis.yuzukitools.api.recipe.YuzukiToolsPlugin;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * ゆづきツールズのレシピ登録管理クラス
 * @author computer
 *
 */
public class RecipeManager {
	
	/**
	 * Pluginインスタンス管理
	 */
	public static Map<EventPriority, List<IYuzukiToolsPlugin>> priorityPluginList = new HashMap<>(); 
	
	/**
	 * preInitで処理を行う
	 */
	public static void preInitRegisterPlugin(FMLPreInitializationEvent event) {
		
		ASMDataTable asmDataTable = event.getAsmData();
		
    	//家庭菜園
    	Set<ASMData> asmDatas = asmDataTable.getAll(YuzukiToolsPlugin.class.getCanonicalName());
    	
    	//対象を取得する
    	for (ASMData asmData : asmDatas) {
    		try {
    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder enumHolderPriority = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;
    			
    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = enumHolderPriority == null ? priority : EventPriority.valueOf(enumHolderPriority.getValue());
    			
    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {
    				
    				Class<?> asmClass = Class.forName(asmData.getClassName());
        			Class<? extends IYuzukiToolsPlugin> asmInstanceClass = asmClass.asSubclass(IYuzukiToolsPlugin.class);
        			
        			//優先度によって格納場所を変更
        			List<IYuzukiToolsPlugin> pluginList = priorityPluginList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmInstanceClass.newInstance());
        			priorityPluginList.put(priority, pluginList);
    			}
    		} catch (Exception e) {
    			YuzukiTools.logger.error("plugin load error " + asmData.getClassName());
			}
    	}
	}
	
	/**
	 * preInitで処理を行う
	 */
	public static void postInitRegisterPlugin(FMLPostInitializationEvent event) {
		
		//レシピ登録インスタンス
		KitchenGardenRegistry register = new KitchenGardenRegistry();
		
		//優先度
		List<EventPriority> priorityList = new ArrayList<>();
		priorityList.add(EventPriority.LOWEST);
		priorityList.add(EventPriority.LOW);
		priorityList.add(EventPriority.NORMAL);
		priorityList.add(EventPriority.HIGH);
		priorityList.add(EventPriority.HIGHEST);
		
		//優先度順でレシピ登録処理を行う
		for (EventPriority priority : priorityList) {
			List<IYuzukiToolsPlugin> pluginList = priorityPluginList.getOrDefault(priority, new ArrayList<>());
			
			for (IYuzukiToolsPlugin plugin : pluginList) {
				plugin.registerKitchenGardenRecipe(register);
			}
		}
		
		//インスタンス破棄
		priorityList = null;
	}

}

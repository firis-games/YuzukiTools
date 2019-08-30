package firis.yuzukitools.common.recipe;

import java.util.List;

import com.google.gson.Gson;

import firis.core.common.helper.ResourceHelper;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.api.recipe.IYuzukiToolsPlugin;
import firis.yuzukitools.api.recipe.JsonRecipesKitchenGarden;
import firis.yuzukitools.api.recipe.KitchenGardenRegistry;
import firis.yuzukitools.api.recipe.YuzukiToolsPlugin;
import firis.yuzukitools.common.config.YKConfig;

@YuzukiToolsPlugin
public class JsonKitchenGardenRecipePlugin implements IYuzukiToolsPlugin {

	/**
	 * 家庭菜園レシピ登録
	 * Json設定ファイルより設定を行う
	 */
	@Override
	public void registerKitchenGardenRecipe(KitchenGardenRegistry registry) {
		
		//Jsonレシピが無効な場合は登録を行わない
		if (YKConfig.USE_JSON_RECIPE != true) return;

		//Resourcesからレシピ登録
		registryJsonRecipe(registry);
		
		//Configファイルからレシピ登録
		registryJsonConfigRecipe(registry);
	}
	
	/**
	 * Resourcesからレシピ登録
	 */
	private void registryJsonRecipe(KitchenGardenRegistry registry) {
		//レシピリストを取得
		List<String> recipeList = ResourceHelper.getResourceList("assets/" + YuzukiTools.MODID + "/mod/kitchen_garden_recipes");

		Gson gson = new Gson();
		for (String recipe : recipeList) {
			
			String json = ResourceHelper.getResourceString(recipe);
			
			//jsonオブジェクト化
			JsonRecipesKitchenGarden jsonRecipe = gson.fromJson(json, JsonRecipesKitchenGarden.class);

			//レシピ登録
			registry.register(jsonRecipe);
		}
	}
	
	/**
	 * Configファイルからレシピ登録
	 */
	public static void registryJsonConfigRecipe(KitchenGardenRegistry registry) {
		//レシピリストを取得
		List<String> recipeList = ResourceHelper.getConfigFileList("recipes_kitchen_garden");
		//Configレシピリストを取得
		ResourceHelper.getConfigFileList("recipes_kitchen_garden");

		Gson gson = new Gson();
		for (String recipe : recipeList) {
			
			String json = ResourceHelper.getConfigFileString(recipe);
			
			//jsonオブジェクト化
			JsonRecipesKitchenGarden jsonRecipe = gson.fromJson(json, JsonRecipesKitchenGarden.class);

			//レシピ登録
			registry.register(jsonRecipe);
		}
	}
}

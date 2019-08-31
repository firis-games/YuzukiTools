package firis.yuzukitools.plugin.client.jei;

import javax.annotation.Nonnull;

import firis.yuzukitools.api.YuzukiToolsAPI;
import firis.yuzukitools.api.recipe.RecipesKitchenGarden;
import firis.yuzukitools.client.gui.YKGuiElectricFurnace;
import firis.yuzukitools.client.gui.YKGuiKitchenGarden;
import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
import firis.yuzukitools.plugin.client.jei.recipe.JEIElectricFurnaceRecipeCategory;
import firis.yuzukitools.plugin.client.jei.recipe.JEIElectricFurnaceRecipeWrapper;
import firis.yuzukitools.plugin.client.jei.recipe.JEIKitchenGardenRecipeCategory;
import firis.yuzukitools.plugin.client.jei.recipe.JEIKitchenGardenRecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 * JEI連携プラグイン
 * @author computer
 *
 */
@JEIPlugin
public class JEIYuzukiToolsPlugin implements IModPlugin {

	/**
	 * レシピのカテゴリー登録
	 */
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {

		//電気炉
		registry.addRecipeCategories(new JEIElectricFurnaceRecipeCategory(registry.getJeiHelpers().getGuiHelper()));

		//家庭菜園
		registry.addRecipeCategories(new JEIKitchenGardenRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		
	}
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		
		//電気炉レシピ登録
		registry.handleRecipes(RecipesElectricFurnace.class, 
				JEIElectricFurnaceRecipeWrapper::new, 
				JEIElectricFurnaceRecipeCategory.UID);
		
		registry.addRecipes(RecipesElectricFurnace.recipes, JEIElectricFurnaceRecipeCategory.UID);
		
		//家庭菜園レシピ登録
		registry.handleRecipes(RecipesKitchenGarden.class, 
				JEIKitchenGardenRecipeWrapper::new, 
				JEIKitchenGardenRecipeCategory.UID);
		
		registry.addRecipes(YuzukiToolsAPI.kitchenGardenRecipes, JEIKitchenGardenRecipeCategory.UID);
		
		
		//「レシピを見る」の設定
		registry.addRecipeClickArea(YKGuiElectricFurnace.class, 79, 34, 24, 17, JEIElectricFurnaceRecipeCategory.UID);
		registry.addRecipeClickArea(YKGuiKitchenGarden.class, 81, 21, 24, 17, JEIKitchenGardenRecipeCategory.UID);
		
	}
}
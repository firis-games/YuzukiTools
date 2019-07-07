package firis.yuzukitools.api.client.jei;

import javax.annotation.Nonnull;

import firis.yuzukitools.api.client.jei.electricfurnace.JEIElectricFurnaceRecipeCategory;
import firis.yuzukitools.api.client.jei.electricfurnace.JEIElectricFurnaceRecipeWrapper;
import firis.yuzukitools.client.gui.YKGuiElectricFurnace;
import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
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
		
	}
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		
		//電気炉レシピ登録
		registry.handleRecipes(RecipesElectricFurnace.class, 
				JEIElectricFurnaceRecipeWrapper::new, 
				JEIElectricFurnaceRecipeCategory.UID);
		
		registry.addRecipes(RecipesElectricFurnace.recipes, JEIElectricFurnaceRecipeCategory.UID);
		
		//「レシピを見る」の設定
		registry.addRecipeClickArea(YKGuiElectricFurnace.class, 79, 34, 24, 17, JEIElectricFurnaceRecipeCategory.UID);
		
	}
}

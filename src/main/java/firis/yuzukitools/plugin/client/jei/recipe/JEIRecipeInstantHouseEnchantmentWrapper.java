package firis.yuzukitools.plugin.client.jei.recipe;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.common.recipe.RecipeInstantHouse;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

/**
 * インスタントハウスのカスタムレシピ
 * @author computer
 *
 */
public class JEIRecipeInstantHouseEnchantmentWrapper implements IRecipeWrapper {

	protected List<ItemStack> inputsItemStack;
	protected ItemStack outputItemStack;
	
	/**
	 * コンストラクタ
	 * @param recipe
	 */
	public JEIRecipeInstantHouseEnchantmentWrapper(RecipeInstantHouse recipe) {
		
		//クラフトアイテム設定
		inputsItemStack = new ArrayList<>();
		inputsItemStack.addAll(recipe.recipes);
		
		outputItemStack = recipe.resultStack;
		
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, inputsItemStack);
		ingredients.setOutput(ItemStack.class, outputItemStack);
	}

}

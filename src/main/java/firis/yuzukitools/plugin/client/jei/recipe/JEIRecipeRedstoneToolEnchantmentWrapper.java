package firis.yuzukitools.plugin.client.jei.recipe;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.common.recipe.RecipeRedstoneToolEnchantment;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

/**
 * レッドストーンツールのカスタムレシピ
 * @author computer
 *
 */
public class JEIRecipeRedstoneToolEnchantmentWrapper implements IRecipeWrapper {

	protected List<ItemStack> inputsItemStack;
	protected ItemStack outputItemStack;
	
	/**
	 * コンストラクタ
	 * @param recipe
	 */
	public JEIRecipeRedstoneToolEnchantmentWrapper(RecipeRedstoneToolEnchantment recipe) {
		
		//クラフトアイテム設定
		inputsItemStack = new ArrayList<>();
		inputsItemStack.add(new ItemStack(recipe.redstoneTool));
		inputsItemStack.add(new ItemStack(recipe.medalItem));
		inputsItemStack.addAll(recipe.catalystStackList);
		
		outputItemStack = recipe.resultStack;
		
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, inputsItemStack);
		ingredients.setOutput(ItemStack.class, outputItemStack);
	}

}

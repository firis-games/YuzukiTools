package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * 電気炉レシピ
 */
public class RecipesElectricFurnace {

	/**
	 * レシピリスト
	 */
	public static List<RecipesElectricFurnace> recipes = new ArrayList<RecipesElectricFurnace>();
	
	/**
	 * レシピの初期化
	 */
	public static void init() {
	
		//鉄鉱石2倍化
		recipes.add(new RecipesElectricFurnace(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT, 2)));
		
	}
	
	/**
	 * レシピに一致するかの判断を行う
	 * @param stack
	 * @return
	 */
	public static RecipesElectricFurnace getRecipe(ItemStack stack) {
		for(RecipesElectricFurnace recipe : RecipesElectricFurnace.recipes) {
			if (stack.getItem() == recipe.getInputItemStack().getItem()
					&& stack.getMetadata() == recipe.getInputItemStack().getMetadata()) {
				return recipe;
			}
		}
		return null;
	}
	
	
	/**
	 * レシピに一致するかの判断を行う
	 * 一致した場合は燃焼時間を返却する
	 * @param stack
	 * @return
	 */
	public static ItemStack getOutputItemStack(ItemStack stack) {
		
		RecipesElectricFurnace recipe = RecipesElectricFurnace.getRecipe(stack);
		
		if (recipe == null) return ItemStack.EMPTY.copy();
		
		return recipe.getOutputItemStack();
	}
	
	
	/**
	 * 材料
	 */
	protected ItemStack inputStack;
	public ItemStack getInputItemStack() {
		return inputStack.copy();
	}
	
	/**
	 * 結果
	 */
	protected ItemStack outputStack;
	public ItemStack getOutputItemStack() {
		return outputStack.copy();
	}
	
	/**
	 * 燃焼時間Tick
	 */
	protected int burnTime;
	public int getBurnTime() {
		return this.burnTime;
	}
	
	/**
	 * コンストラクタ
	 */
	public RecipesElectricFurnace(ItemStack inputStack, ItemStack outputStack) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.burnTime = 400;
	}
	
}

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
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Blocks.IRON_ORE), 
				new ItemStack(Items.IRON_INGOT, 2)));

		//金鉱石2倍化
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Blocks.GOLD_ORE), 
				new ItemStack(Items.GOLD_INGOT, 2)));
		
		//ダイヤ鉱石増量
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Blocks.DIAMOND_ORE), 
				new ItemStack(Items.DIAMOND, 6)));
		
		//レッドストーン鉱石増量
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Blocks.REDSTONE_ORE), 
				new ItemStack(Items.REDSTONE, 8)));
		
		//ラピスラズリ鉱石増量
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Blocks.LAPIS_ORE), 
				new ItemStack(Items.DYE, 12, 4)));
		
		//鉄の斧
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_AXE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 3),
				800));
		
		//鉄のクワ
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_HOE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 2),
				800));
		//鉄のツルハシ
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_PICKAXE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 3),
				800));
		
		//鉄のシャベル
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_SHOVEL, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 1),
				800));
		
		//鉄の剣
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_SWORD, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 2),
				800));
	
		//鉄の防具
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_HELMET, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 5),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_CHESTPLATE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 8),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_LEGGINGS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 7),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.IRON_BOOTS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 4),
				800));
		
		//金の斧
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_AXE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 3),
				800));
		
		//金のクワ
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_HOE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 2),
				800));
		
		//金のツルハシ
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_PICKAXE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 3),
				800));
		
		//金のシャベル
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_SHOVEL, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 1),
				800));
		
		//金の剣
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_SWORD, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 2),
				800));
		
		//金の防具
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_HELMET, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 5),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_CHESTPLATE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 8),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_LEGGINGS, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 7),
				800));
		recipes.add(new RecipesElectricFurnace(
				new ItemStack(Items.GOLDEN_BOOTS, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 4),
				800));
	}
	
	/**
	 * レシピに一致するかの判断を行う
	 * @param stack
	 * @return
	 */
	public static RecipesElectricFurnace getRecipe(ItemStack stack) {
		for(RecipesElectricFurnace recipe : RecipesElectricFurnace.recipes) {
			if (stack.getItem() == recipe.getInputItemStack().getItem()
					&& (stack.getMetadata() == recipe.getInputItemStack().getMetadata()
							|| recipe.getInputItemStack().getMetadata() == 32767)) {
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
	
	/**
	 * コンストラクタ
	 */
	public RecipesElectricFurnace(ItemStack inputStack, ItemStack outputStack, int burnTime) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.burnTime = burnTime;
	}
	
}

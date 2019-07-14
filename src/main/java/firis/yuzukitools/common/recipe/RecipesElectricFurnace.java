package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

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
		RecipesElectricFurnace.register(
				"oreIron", 
				new ItemStack(Items.IRON_INGOT, 2));

		//金鉱石2倍化
		RecipesElectricFurnace.register(
				"oreGold", 
				new ItemStack(Items.GOLD_INGOT, 2));
		
		//ダイヤ鉱石増量
		RecipesElectricFurnace.register(
				new ItemStack(Blocks.DIAMOND_ORE), 
				new ItemStack(Items.DIAMOND, 5));
		
		//レッドストーン鉱石増量
		RecipesElectricFurnace.register(
				new ItemStack(Blocks.REDSTONE_ORE), 
				new ItemStack(Items.REDSTONE, 8));

		//ラピスラズリ鉱石増量
		RecipesElectricFurnace.register(
				new ItemStack(Blocks.LAPIS_ORE), 
				new ItemStack(Items.DYE, 10, 4));
		
		//鉄の斧
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_AXE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		
		//鉄のクワ
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_HOE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 2),
				800);
		//鉄のツルハシ
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_PICKAXE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		
		//鉄のシャベル
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_SHOVEL, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 1),
				800);
		
		//鉄の剣
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_SWORD, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 2),
				800);
	
		//鉄の防具
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_HELMET, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 5),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_CHESTPLATE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 8),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_LEGGINGS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 7),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.IRON_BOOTS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 4),
				800);
		
		//金の斧
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_AXE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 3),
				800);
		
		//金のクワ
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_HOE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 2),
				800);
		
		//金のツルハシ
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_PICKAXE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 3),
				800);
		
		//金のシャベル
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_SHOVEL, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 1),
				800);
		
		//金の剣
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_SWORD, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 2),
				800);
		
		//金の防具
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_HELMET, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 5),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_CHESTPLATE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 8),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_LEGGINGS, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 7),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.GOLDEN_BOOTS, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 4),
				800);
		
		//チェイン防具
		RecipesElectricFurnace.register(
				new ItemStack(Items.CHAINMAIL_HELMET, 1, 32767), 
				new ItemStack(Items.IRON_NUGGET, 5),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, 32767), 
				new ItemStack(Items.IRON_NUGGET, 8),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, 32767), 
				new ItemStack(Items.IRON_NUGGET, 7),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.CHAINMAIL_BOOTS, 1, 32767), 
				new ItemStack(Items.IRON_NUGGET, 4),
				800);
		
		//ハンマーアックス
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.IRON_HAMMERAXE, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 4),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.GOLD_HAMMERAXE, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 4),
				800);
		
		//シールドソード
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.IRON_SHIELD_SWORD, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.GOLD_SHIELD_SWORD, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 3),
				800);
		
		//レッドストーンツール
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_AXE, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_HOE, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 2),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_PICKAXE, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_SHOVEL, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 1),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_SWORD, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 2),
				800);
		
		//レッドストーン防具
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_HELMET, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 5),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_CHESTPLATE, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 8),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_LEGGINGS, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 7),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(YKItems.REDSTONE_BOOTS, 1, 32767), 
				new ItemStack(YKItems.REDSTONE_INGOT, 4),
				800);
		
		//小物
		RecipesElectricFurnace.register(
				new ItemStack(Items.BUCKET, 1), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.WATER_BUCKET, 1), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.LAVA_BUCKET, 1), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.MILK_BUCKET, 1), 
				new ItemStack(Items.IRON_INGOT, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Blocks.ANVIL, 1, 32767), 
				new ItemStack(Blocks.IRON_BLOCK, 3),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.SHEARS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 2),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.COMPASS, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 4),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.CLOCK, 1, 32767), 
				new ItemStack(Items.GOLD_INGOT, 4),
				800);
		RecipesElectricFurnace.register(
				new ItemStack(Items.SHIELD, 1, 32767), 
				new ItemStack(Items.IRON_INGOT, 1),
				800);
		
		//MOD連携用
		//鋼鉄インゴット
		RecipesElectricFurnace.register(
				"ingotIron", 
				"ingotSteel",
				1,
				400);
		
		//鉱石2倍化
		List<String> doublingOreList = new ArrayList<String>();
		//銅鉱石
		doublingOreList.add("Copper");
		//鉛鉱石
		doublingOreList.add("Lead");
		//錫鉱石
		doublingOreList.add("Tin");
		//銀鉱石
		doublingOreList.add("Silver");
		//ニッケル鉱石
		doublingOreList.add("Nickel");
		//ウラニウム鉱石
		doublingOreList.add("Uranium");
		//アルミニウム鉱石
		doublingOreList.add("Aluminum");
		//オスミウム鉱石
		doublingOreList.add("Osmium");
		for (String doublingOre : doublingOreList) {
			//2倍化
			RecipesElectricFurnace.register(
					"ore" + doublingOre, 
					"ingot" + doublingOre, 
					2,
					400);
		}
		
		//基本回路
		OreDictionary.registerOre("circuitBasic", YKItems.REDSTONE_INGOT);
	}
	
	/**
	 * レシピの登録はregisterからのみ行うようにする
	 */
	
	/**
	 * 外向け用レシピ登録メソッド
	 * @param inputStack
	 * @param outputStack
	 */
	public static void register(ItemStack inputStack, ItemStack outputStack) {
		commonRegister(inputStack, outputStack, 400);
	}
	public static void register(ItemStack inputStack, ItemStack outputStack, int burnTime) {
		commonRegister(inputStack, outputStack, burnTime);
	}
	public static void register(String oreName, ItemStack outputStack) {
		commonRegister(oreName, outputStack, 400);
	}
	public static void register(String oreName, ItemStack outputStack, int burnTime) {
		commonRegister(oreName, outputStack, burnTime);
	}
	
	/**
	 * その他MOD連携用
	 * @param oreName
	 * @param outputStack
	 * @param burnTime
	 */
	public static void register(String inOreName, String outOreName, int outCount, int burnTime) {
		//outputが存在する場合のみレシピ登録する
		NonNullList<ItemStack> oreList = OreDictionary.getOres(outOreName);
		if(oreList.size() > 0) {
			ItemStack outputStack = oreList.get(0);
			outputStack.setCount(outCount);
			commonRegister(inOreName, outputStack, burnTime);
		}
	}
	
	/**
	 * 通常レシピと鉱石辞書でのレシピ登録を行う
	 * @param inputStack
	 * @param outputStack
	 * @param burnTime
	 */
	private static void commonRegister(Object inputStack, ItemStack outputStack, int burnTime) {
		recipes.add(new RecipesElectricFurnace(
				inputStack, 
				outputStack,
				burnTime));
	}
	
	//******************************************************************************************
	
	/**
	 * レシピに一致するかの判断を行う
	 * @param stack
	 * @return
	 */
	public static RecipesElectricFurnace getRecipe(ItemStack stack) {
		for(RecipesElectricFurnace recipe : RecipesElectricFurnace.recipes) {
			//レシピ複数分を回してチェック
			for (ItemStack inputStack : recipe.getInputItemStackList()) {
				if (stack.getItem() == inputStack.getItem()
						&& (stack.getMetadata() == inputStack.getMetadata()
								|| inputStack.getMetadata() == 32767)) {
					return recipe;
				}
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
	 * 材料（鉱石辞書対応）
	 */
	protected Object inputStack;
	public List<ItemStack> getInputItemStackList() {
		List<ItemStack> inputList = new ArrayList<ItemStack>();
		if (this.inputStack instanceof ItemStack) {
			//通常のItemStack
			inputList.add(((ItemStack) inputStack).copy());
		} else if (inputStack instanceof String) {
			//鉱石辞書
			NonNullList<ItemStack> oreDict = OreDictionary.getOres((String) inputStack);
			for (ItemStack stack : oreDict) {
				inputList.add(stack.copy());
			}
		}
		return inputList;
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
	 * コンストラクタ(外部から直接呼び出さないようにする)
	 */
	private RecipesElectricFurnace(Object inputStack, ItemStack outputStack, int burnTime) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.burnTime = burnTime;
	}
	
}

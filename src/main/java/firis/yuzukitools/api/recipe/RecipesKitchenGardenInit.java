package firis.yuzukitools.api.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import firis.core.common.helper.ResourceHelper;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.api.recipe.RecipesKitchenGarden.SoilType;
import firis.yuzukitools.common.config.YKConfig;
import firis.yuzukitools.common.recipe.json.JsonRecipesKitchenGarden;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IPlantable;

public class RecipesKitchenGardenInit {
	
	/**
	 * レシピの初期化
	 */
	public static void init() {
		
		if (YKConfig.USE_JSON_RECIPE) {
			//Jsonから生成
			initJsonRecipe();
			initJsonConfigRecipe();
		} else {
			//プログラムから生成
			initCreateRecipe();
		}
	}
	
	/**
	 * jsonレシピから初期化
	 */
	public static void initJsonRecipe() {
		//レシピリストを取得
		List<String> recipeList = ResourceHelper.getResourceList("assets/" + YuzukiTools.MODID + "/mod/kitchen_garden_recipes");

		Gson gson = new Gson();
		for (String recipe : recipeList) {
			
			String json = ResourceHelper.getResourceString(recipe);
			
			//jsonオブジェクト化
			JsonRecipesKitchenGarden jsonRecipe = gson.fromJson(json, JsonRecipesKitchenGarden.class);

			//レシピ登録
			RecipesKitchenGarden.register(jsonRecipe);
		}
	}
	
	/**
	 * jsonレシピから初期化
	 */
	public static void initJsonConfigRecipe() {
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
			RecipesKitchenGarden.register(jsonRecipe);
		}
	}
	
	/**
	 * プログラム上でレシピを生成する
	 */
	@SuppressWarnings("deprecation")
	public static void initCreateRecipe() {
		//デフォルト種系のみ自動追加
    	Iterator<ResourceLocation> itemKeys = Item.REGISTRY.getKeys().iterator();
    	while (itemKeys.hasNext()) {
    		ResourceLocation rl = itemKeys.next();
    		Item item = Item.REGISTRY.getObject(rl);
    		//種系アイテム
    		if (item instanceof IPlantable) {
    			RecipesKitchenGarden.register(item);
    		}
    	}
    	
    	//固定登録
    	//ネザーウォート
    	RecipesKitchenGarden.register(new ItemStack(Items.NETHER_WART), 
    			Blocks.NETHER_WART.getDefaultState(), 
    			SoilType.SOUL_SAND,
    			200,
    			0,
    			3,
    			new ItemStack(Items.NETHER_WART));
    	
    	//サトウキビ
    	RecipesKitchenGarden.register(new ItemStack(Items.REEDS), 
    			Blocks.REEDS.getDefaultState(), 
    			SoilType.DIRT_SAND,
    			200,
    			new ItemStack(Items.REEDS));
    	
    	//サボテン
    	RecipesKitchenGarden.register(new ItemStack(Blocks.CACTUS), 
    			Blocks.CACTUS.getDefaultState(), 
    			SoilType.SAND,
    			200,
    			new ItemStack(Blocks.CACTUS));
    	
    	//カボチャ
    	RecipesKitchenGarden.register(new ItemStack(Items.PUMPKIN_SEEDS), 
    			Blocks.PUMPKIN.getDefaultState(), 
    			SoilType.DIRT,
    			200,
    			new ItemStack(Blocks.PUMPKIN));
    	
    	//スイカ
    	RecipesKitchenGarden.register(new ItemStack(Items.MELON_SEEDS), 
    			Blocks.MELON_BLOCK.getDefaultState(), 
    			SoilType.DIRT,
    			200,
    			new ItemStack(Blocks.MELON_BLOCK));
    	
    	//きのこ
    	RecipesKitchenGarden.register(new ItemStack(Blocks.RED_MUSHROOM), 
    			Blocks.RED_MUSHROOM.getDefaultState(), 
    			SoilType.MUSHROOM,
    			200,
    			new ItemStack(Blocks.RED_MUSHROOM));
    	RecipesKitchenGarden.register(new ItemStack(Blocks.BROWN_MUSHROOM), 
    			Blocks.BROWN_MUSHROOM.getDefaultState(), 
    			SoilType.MUSHROOM,
    			200,
    			new ItemStack(Blocks.BROWN_MUSHROOM));
    	
    	//オークの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 0), 
    			Blocks.SAPLING.getStateFromMeta(0), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 4, 0),
    			new ItemStack(Blocks.SAPLING, 1, 0),
    			new ItemStack(Items.APPLE, 1));
    	
    	//マツの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 1), 
    			Blocks.SAPLING.getStateFromMeta(1), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 1),
    			new ItemStack(Blocks.SAPLING, 1, 1));
    	
    	//シラカバの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 2), 
    			Blocks.SAPLING.getStateFromMeta(2), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 2),
    			new ItemStack(Blocks.SAPLING, 1, 2));
    	
    	//ジャングルの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 3), 
    			Blocks.SAPLING.getStateFromMeta(3), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 3),
    			new ItemStack(Blocks.SAPLING, 1, 3));
    	
    	//アカシアの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 4), 
    			Blocks.SAPLING.getStateFromMeta(4), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG2, 5, 0),
    			new ItemStack(Blocks.SAPLING, 1, 4));
    	
    	//ダークオークの苗木
    	RecipesKitchenGarden.register(new ItemStack(Blocks.SAPLING, 1, 5), 
    			Blocks.SAPLING.getStateFromMeta(5), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG2, 5, 1),
    			new ItemStack(Blocks.SAPLING, 1, 5));
    	
    	//お花
    	for (int meta = 0; meta <= 8; meta++) {
    		RecipesKitchenGarden.register(new ItemStack(Blocks.RED_FLOWER, 1, meta), 
	    			Blocks.RED_FLOWER.getStateFromMeta(meta), 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.RED_FLOWER, 1, meta));
    	}
    	for (int meta = 0; meta <= 0; meta++) {
    		RecipesKitchenGarden.register(new ItemStack(Blocks.YELLOW_FLOWER, 1, meta), 
	    			Blocks.YELLOW_FLOWER.getStateFromMeta(meta), 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.YELLOW_FLOWER, 1, meta));
    	}
    	
    	//コーラスフラワー
    	RecipesKitchenGarden.register(new ItemStack(Blocks.CHORUS_FLOWER, 1), 
    			Blocks.CHORUS_FLOWER.getDefaultState(), 
    			SoilType.END_STONE,
    			400,
    			new ItemStack(Blocks.CHORUS_FLOWER, 1),
    			new ItemStack(Items.CHORUS_FRUIT, 2));
    	
    	//2段の高さのお花
    	for (int meta = 0; meta <= 5; meta++) {
    		
    		//表示用BlockState生成
    		List<List<IBlockState>> displayList = new ArrayList<List<IBlockState>>();
    		List<IBlockState> plantStateList = new ArrayList<IBlockState>();
    		plantStateList.add(Blocks.DOUBLE_PLANT.getStateFromMeta(meta));
    		plantStateList.add(Blocks.DOUBLE_PLANT.getStateFromMeta(meta).withProperty(
    				BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER));
    		displayList.add(plantStateList);
    		
    		RecipesKitchenGarden.register(new ItemStack(Blocks.DOUBLE_PLANT, 1, meta), 
	    			displayList, 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.DOUBLE_PLANT, 1, meta));
    	}
    	
	}
}

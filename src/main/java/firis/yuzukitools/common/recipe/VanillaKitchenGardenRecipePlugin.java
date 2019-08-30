package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.api.recipe.IYuzukiToolsPlugin;
import firis.yuzukitools.api.recipe.KitchenGardenRegistry;
import firis.yuzukitools.api.recipe.YuzukiToolsPlugin;
import firis.yuzukitools.common.config.YKConfig;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@YuzukiToolsPlugin
public class VanillaKitchenGardenRecipePlugin implements IYuzukiToolsPlugin {

	/**
	 * 家庭菜園レシピ登録
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void registerKitchenGardenRecipe(KitchenGardenRegistry recipe) {

		//Jsonレシピが有効な場合は登録を行わない
		if (YKConfig.USE_JSON_RECIPE == true) return;
		
		//骨粉
		ItemStack bone_meal = new ItemStack(Items.DYE, 1, 15);
		int progress = 200;
		
		//小麦
		recipe.registerIPlantable(Items.WHEAT_SEEDS, progress);

		//ジャガイモ
		recipe.registerIPlantable(Items.POTATO, progress);

		//ビートルート
		recipe.registerIPlantable(Items.CARROT, progress);

		//ビートルート
		recipe.registerIPlantable(Items.BEETROOT_SEEDS, progress);
		
		//サトウキビ
		recipe.register(new ItemStack(Items.REEDS), 
				toList(Blocks.REEDS.getDefaultState()), 
    			SoilType.DIRT_SAND.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Items.REEDS)),
    			progress);
		
		//サボテン
		recipe.register(new ItemStack(Blocks.CACTUS), 
				toList(Blocks.CACTUS.getDefaultState()), 
    			SoilType.SAND.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.CACTUS)),
    			progress);
    	
    	//カボチャ
		recipe.register(new ItemStack(Items.PUMPKIN_SEEDS), 
				toList(Blocks.PUMPKIN.getDefaultState()), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.PUMPKIN)),
    			progress);
    	
    	//スイカ
		recipe.register(new ItemStack(Items.MELON_SEEDS), 
				toList(Blocks.MELON_BLOCK.getDefaultState()), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.MELON_BLOCK)),
    			progress);
    	
    	//きのこ
		recipe.register(new ItemStack(Blocks.RED_MUSHROOM), 
				toList(Blocks.RED_MUSHROOM.getDefaultState()), 
    			SoilType.MUSHROOM.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.RED_MUSHROOM)),
    			progress);
		recipe.register(new ItemStack(Blocks.BROWN_MUSHROOM), 
				toList(Blocks.BROWN_MUSHROOM.getDefaultState()), 
    			SoilType.MUSHROOM.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.BROWN_MUSHROOM)),
    			progress);
    	
    	//オークの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 0), 
				toList(Blocks.SAPLING.getStateFromMeta(0)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG, 4, 0),
    					new ItemStack(Blocks.SAPLING, 1, 0),
    					new ItemStack(Items.APPLE, 1)),
    			progress * 2);
    	
    	//マツの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 1), 
				toList(Blocks.SAPLING.getStateFromMeta(1)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG, 5, 1),
    					new ItemStack(Blocks.SAPLING, 1, 1)),
    			progress * 2);
    	
    	//シラカバの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 2), 
				toList(Blocks.SAPLING.getStateFromMeta(2)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG, 5, 2),
    					new ItemStack(Blocks.SAPLING, 1, 2)),
    			progress * 2);

    	//ジャングルの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 3), 
				toList(Blocks.SAPLING.getStateFromMeta(3)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG, 5, 3),
    					new ItemStack(Blocks.SAPLING, 1, 3)),
    			progress * 2);

    	
    	//アカシアの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 4), 
				toList(Blocks.SAPLING.getStateFromMeta(4)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG2, 5, 0),
    					new ItemStack(Blocks.SAPLING, 1, 4)),
    			progress * 2);
		
    	
    	//ダークオークの苗木
		recipe.register(new ItemStack(Blocks.SAPLING, 1, 5), 
				toList(Blocks.SAPLING.getStateFromMeta(5)), 
    			SoilType.DIRT.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.LOG2, 5, 1),
    					new ItemStack(Blocks.SAPLING, 1, 5)),
    			progress * 2);
		
    	//お花
    	for (int meta = 0; meta <= 8; meta++) {
    		recipe.register(new ItemStack(Blocks.RED_FLOWER, 1, meta), 
    				toList(Blocks.RED_FLOWER.getStateFromMeta(meta)), 
        			SoilType.DIRT.getSoilList(),
        			toList(bone_meal),
        			toList(new ItemStack(Blocks.RED_FLOWER, 1, meta)),
        			progress);
    	}
    	for (int meta = 0; meta <= 0; meta++) {
    		recipe.register(new ItemStack(Blocks.YELLOW_FLOWER, 1, meta), 
    				toList(Blocks.YELLOW_FLOWER.getStateFromMeta(meta)), 
        			SoilType.DIRT.getSoilList(),
        			toList(bone_meal),
        			toList(new ItemStack(Blocks.YELLOW_FLOWER, 1, meta)),
        			progress);
    	}
    	
    	//コーラスフラワー
    	recipe.register(new ItemStack(Blocks.CHORUS_FLOWER, 1), 
				toList(Blocks.CHORUS_FLOWER.getDefaultState()), 
    			SoilType.END_STONE.getSoilList(),
    			toList(bone_meal),
    			toList(new ItemStack(Blocks.CHORUS_FLOWER, 1),
    	    			new ItemStack(Items.CHORUS_FRUIT, 2)),
    			progress * 2);
    	
    	//2段の高さのお花
    	for (int meta = 0; meta <= 5; meta++) {
    		
    		//表示用BlockState生成
    		List<List<IBlockState>> displayList = new ArrayList<List<IBlockState>>();
    		List<IBlockState> plantStateList = new ArrayList<IBlockState>();
    		plantStateList.add(Blocks.DOUBLE_PLANT.getStateFromMeta(meta));
    		plantStateList.add(Blocks.DOUBLE_PLANT.getStateFromMeta(meta).withProperty(
    				BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER));
    		displayList.add(plantStateList);
    		
    		recipe.register(new ItemStack(Blocks.DOUBLE_PLANT, 1, meta), 
    				displayList, 
        			SoilType.DIRT.getSoilList(),
        			toList(bone_meal),
        			toList(new ItemStack(Blocks.DOUBLE_PLANT, 1, meta)),
        			progress);
    	}
		
	}
	
	/**
	 * レシピ登録用のヘルパーメソッド
	 * @param state
	 * @return
	 */
	public static List<List<IBlockState>> toList(IBlockState... states) {
		List<List<IBlockState>> list = new ArrayList<>();
		for (IBlockState state : states) {
			List<IBlockState> work = new ArrayList<>();
			work.add(state);
			list.add(work);
		}
		return list;
	}
	
	/**
	 * レシピ登録用のヘルパーメソッド
	 * @param state
	 * @return
	 */
	public static List<ItemStack> toList(ItemStack... stacks) {
		List<ItemStack> list = new ArrayList<>();
		for (ItemStack stack : stacks) {
			list.add(stack);
		}
		return list;
	}
	
	
	/**
	 * 汎用登録用土壌タイプ設定
	 * @author computer
	 */
	public enum SoilType {
		DIRT(new ItemStack(Blocks.GRASS, 1, 32767), 
				new ItemStack(Blocks.DIRT, 1, 32767)),
		SAND(new ItemStack(Blocks.SAND, 1, 32767)),
		DIRT_SAND(new ItemStack(Blocks.GRASS, 1, 32767), 
				new ItemStack(Blocks.DIRT, 1, 32767),
				new ItemStack(Blocks.SAND, 1, 32767)),
		MUSHROOM(new ItemStack(Blocks.GRASS, 1, 32767), 
				new ItemStack(Blocks.DIRT, 1, 32767),
				new ItemStack(Blocks.STONE, 1, 32767),
				new ItemStack(Blocks.LOG, 1, 32767),
				new ItemStack(Blocks.LOG2, 1, 32767)),
		SOUL_SAND(new ItemStack(Blocks.SOUL_SAND, 1, 32767)),
		JUNGLE_WOOD(new ItemStack(Blocks.LOG, 1, 3)),
		END_STONE(new ItemStack(Blocks.END_STONE, 1)),
		;
		
		private SoilType(ItemStack... doils) {
			this.soilList = new ArrayList<ItemStack>();
			for (ItemStack soil : doils) {
				soilList.add(soil);
			}
		}
		private List<ItemStack> soilList;
		public List<ItemStack> getSoilList() {
			return this.soilList;
		}
	}
}

package firis.yuzukitools.api.recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import firis.core.common.helper.JsonHelper;
import firis.core.common.helper.ReflectionHelper;
import firis.core.common.helper.JsonHelper.JsonHelperException;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.api.YuzukiToolsAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * 家庭菜園レシピ登録用クラス
 * @author computer
 *
 */
public class KitchenGardenRegistry {
	
	/**
	 * レシピ登録
	 */
	public void register(ItemStack seed, List<List<IBlockState>> displayList, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress) {
		commonRegister(seed, displayList, soilList, fertilizerList, harvestList, progress);
	}
	
	/**
	 * 内部共通レシピ登録処理
	 * @param seed 種
	 * @param stateSeedList 種描画用リスト
	 * @param soilList 土壌リスト
	 * @param fertilizerList 肥料リスト
	 * @param harvestList 収穫物リスト
	 * @param progress 育成時間
	 */
	private void commonRegister(ItemStack seed, List<List<IBlockState>> stateSeedList, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress) {
		YuzukiToolsAPI.kitchenGardenRecipes.add(new RecipesKitchenGarden(
				seed, 
				stateSeedList,
				soilList,
				fertilizerList,
				harvestList,
				progress));
	}
	
	/**
	 * 汎用登録用土壌タイプ設定
	 * @author computer
	 */
	public static enum SoilType {
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
	
	/**
	 * 種系アイテムのみ処理を行う
	 * @param inputStack
	 * @param outputStack
	 */
	@SuppressWarnings("deprecation")
	public void registerIPlantable(Item itemPlant, int progress) {
		try {
			
			IPlantable plant;
			if (itemPlant instanceof IPlantable) {
				plant = (IPlantable) itemPlant;
			} else {
				return;
			}			
			
			//骨粉
			ItemStack bone_meal = new ItemStack(Items.DYE, 1, 15);
			
			//植物タイプを取得
			EnumPlantType plantType = plant.getPlantType(null, null);
			
			//土壌を判断する
			Block soil = null;
			if (EnumPlantType.Crop.equals(plantType)) {
				//土壌は耕地でなく土を使う
				soil = Blocks.DIRT;
			} else if(EnumPlantType.Nether.equals(plantType)){
				soil = Blocks.NETHER_BRICK;
			}
			
			//土壌判定できない場合は登録しない
			if (soil == null) return;
			
			//描画用
			IBlockState plantState = plant.getPlant(null, null);
			
			//判断
			if (!(plantState.getBlock() instanceof BlockCrops)) return;
			
			//収穫物を取得
			Item cropItem = null;
			Method method = ReflectionHelper.findMethod(plantState.getBlock().getClass(), "getCrop", "func_149865_P");
			try {
				cropItem = (Item) method.invoke(plantState.getBlock());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
			if (cropItem == null) return;
			
			//レシピ設定用
			List<ItemStack> soilList = new ArrayList<ItemStack>();
			List<ItemStack> fertilizerList = new ArrayList<ItemStack>();
			List<ItemStack> harvestList = new ArrayList<ItemStack>();
			
			//土壌
			soilList.add(new ItemStack(soil));
	
			//肥料
			fertilizerList.add(bone_meal.copy());
			
			//収穫物
			if (cropItem != plant) {
				harvestList.add(new ItemStack(cropItem, 1));
				harvestList.add(new ItemStack(itemPlant));
			} else {
				harvestList.add(new ItemStack(cropItem, 2));
			}
	
			//収穫までの時間（200tick）
			int minAge = 0;
			int maxAge = ((BlockCrops)(plantState.getBlock())).getMaxAge();

			List<List<IBlockState>> stateSeedList = new ArrayList<List<IBlockState>>();
			if (minAge == 0 && maxAge == 0) {
				List<IBlockState> tempSeed = new ArrayList<IBlockState>();
				tempSeed.add(plantState);
				stateSeedList.add(tempSeed);
			} else {
				//Age分のメタデータを設定する
				for (int meta = minAge; meta <= maxAge; meta++) {
					List<IBlockState> tempSeed = new ArrayList<IBlockState>();
					tempSeed.add(plantState.getBlock().getStateFromMeta(meta));
					stateSeedList.add(tempSeed);
				}
			}
	
			//種をレシピ登録する
			register(new ItemStack(itemPlant),
					stateSeedList,
					soilList,
					fertilizerList,
					harvestList,
					progress);
			
		} catch(Exception e) {
			YuzukiTools.logger.error("Kitchen Garden IPlantable Register Error:" + itemPlant.getRegistryName().toString());
		}
	}
	
	/**
	 * Jsonオブジェクトからレシピ登録
	 * @param seed
	 * @param stateSeedList
	 * @param soilType
	 * @param progress
	 * @param harvest
	 */
	public void register(JsonRecipesKitchenGarden jsonRecipe) {
		
		try {
			//種
			ItemStack seed = JsonHelper.fromStringItemStack(jsonRecipe.seed);
			
			//描画用オブジェクト
			List<List<IBlockState>> display = new ArrayList<>();
			for (List<String> stage : jsonRecipe.display) {
				List<IBlockState> stageList = new ArrayList<>();
				for (String state : stage) {
					stageList.add(JsonHelper.fromStringBlockState(state));
				}
				display.add(stageList);
			}
			
			//土壌
			List<ItemStack> soilList = fromStringItemStackList(jsonRecipe.soil);
			
			//肥料
			List<ItemStack> fertilizerList = fromStringItemStackList(jsonRecipe.fertilizer);
			
			//収穫物
			List<ItemStack> harvestList = fromStringItemStackList(jsonRecipe.harvest);
			
			//育成時間
			int progress = jsonRecipe.progress;
			
			//レシピ登録
			YuzukiToolsAPI.kitchenGardenRecipes.add(new RecipesKitchenGarden(
					seed, 
					display,
					soilList,
					fertilizerList,
					harvestList,
					progress));
			
		} catch (JsonHelperException exception) {
			//レシピ登録失敗
			
		}
	}
	
	/**
	 * 文字列リストからItemStackリストを生成する
	 * @param String
	 * @return
	 * @throws JsonHelperException 
	 */
	private List<ItemStack> fromStringItemStackList(List<String> itemList) throws JsonHelperException {
		List<ItemStack> itemStackList = new ArrayList<>();
		
		for (String item : itemList) {
			itemStackList.add(JsonHelper.fromStringItemStack(item));
		}
		
		return itemStackList;
	}
	
}

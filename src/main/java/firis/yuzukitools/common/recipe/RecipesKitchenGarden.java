package firis.yuzukitools.common.recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import firis.core.common.helper.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class RecipesKitchenGarden {

	/**
	 * レシピリスト
	 */
	public static List<RecipesKitchenGarden> recipes = new ArrayList<RecipesKitchenGarden>();
	
	/**
	 * 肥料用骨粉
	 */
	protected static ItemStack bone_meal = new ItemStack(Items.DYE, 1, 15);
	
	/**
	 * レシピの初期化
	 */
	@SuppressWarnings("deprecation")
	public static void init() {
		//デフォルト種系のみ自動追加
    	Iterator<ResourceLocation> itemKeys = Item.REGISTRY.getKeys().iterator();
    	while (itemKeys.hasNext()) {
    		ResourceLocation rl = itemKeys.next();
    		Item item = Item.REGISTRY.getObject(rl);
    		//種系アイテム
    		if (item instanceof IPlantable) {
    			register(item);
    		}
    	}
    	
    	//固定登録
    	//サトウキビ
    	register(new ItemStack(Items.REEDS), 
    			Blocks.REEDS.getDefaultState(), 
    			SoilType.DIRT_SAND,
    			200,
    			new ItemStack(Items.REEDS));
    	
    	//サボテン
    	register(new ItemStack(Blocks.CACTUS), 
    			Blocks.CACTUS.getDefaultState(), 
    			SoilType.SAND,
    			200,
    			new ItemStack(Blocks.CACTUS));
    	
    	//オークの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 0), 
    			Blocks.SAPLING.getStateFromMeta(0), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 0),
    			new ItemStack(Blocks.SAPLING, 1, 0),
    			new ItemStack(Items.APPLE, 1));
    	
	}
	
	/**
	 * 種系アイテムのみ処理を行う
	 * @param inputStack
	 * @param outputStack
	 */
	public static void register(Item itemPlant) {
		
		IPlantable plant;
		if (itemPlant instanceof IPlantable) {
			plant = (IPlantable) itemPlant;
		} else {
			return;
		}
		
		//植物タイプを取得
		EnumPlantType plantType = plant.getPlantType(null, null);
		
		//土壌を判断する
		Block soil = null;
		if (EnumPlantType.Crop.equals(plantType)) {
			//土壌は耕地でなく土を使う
			//soil = Blocks.FARMLAND;
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
		harvestList.add(new ItemStack(cropItem));
		harvestList.add(new ItemStack(itemPlant));

		//収穫までの時間（200tick）
		int maxAge = ((BlockCrops)(plantState.getBlock())).getMaxAge();
		int progress = 200;

		//種をレシピ登録する
		commonRegister(
				new ItemStack(itemPlant),
				plantState,
				soilList,
				fertilizerList,
				harvestList,
				progress,
				0,
				maxAge
				);
	}
	
	
	/**
	 * 汎用登録用土壌タイプ設定
	 * @author computer
	 */
	public static enum SoilType {
		DIRT,
		SAND,
		DIRT_SAND;
	}
	/**
	 * 汎用登録用
	 * 骨粉対応
	 */
	public static void register(ItemStack seed, IBlockState seedState, SoilType soilType, int progress, ItemStack... harvest) {
		
		//レシピ設定用
		List<ItemStack> soilList = new ArrayList<ItemStack>();
		List<ItemStack> fertilizerList = new ArrayList<ItemStack>();
		List<ItemStack> harvestList = new ArrayList<ItemStack>();
		
		//土壌
		if (soilType == SoilType.DIRT) {
			soilList.add(new ItemStack(Blocks.GRASS, 1, 32767));
			soilList.add(new ItemStack(Blocks.DIRT, 1, 32767));
		} else if (soilType == SoilType.SAND) {
			soilList.add(new ItemStack(Blocks.SAND, 1, 32767));			
		} else if (soilType == SoilType.DIRT_SAND) {
			soilList.add(new ItemStack(Blocks.GRASS, 1, 32767));
			soilList.add(new ItemStack(Blocks.DIRT, 1, 32767));
			soilList.add(new ItemStack(Blocks.SAND, 1, 32767));			
		}

		//肥料
		fertilizerList.add(bone_meal.copy());
		
		//収穫物
		for(ItemStack stack : harvest) {
			harvestList.add(stack.copy());
		}

		//種をレシピ登録する
		commonRegister(
				seed,
				seedState,
				soilList,
				fertilizerList,
				harvestList,
				progress,
				0,
				0
				);
	}
	
	/**
	 * 種系レシピ登録を行う
	 * @param inputStack
	 * @param outputStack
	 * @param burnTime
	 */
	private static void commonRegister(ItemStack seed, IBlockState seedState, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress, int minAge, int maxAge) {
		recipes.add(new RecipesKitchenGarden(
				seed, 
				seedState,
				soilList,
				fertilizerList,
				harvestList,
				progress,
				minAge,
				maxAge));
	}
	
	/**
	 * レシピに一致するかの判断を行う
	 * @param stack
	 * @return
	 */
	public static RecipesKitchenGarden getRecipe(ItemStack seed, ItemStack soil) {
		
		if (seed.isEmpty() || soil.isEmpty()) return null;
		
		for(RecipesKitchenGarden recipe : RecipesKitchenGarden.recipes) {
			//種の判断
			ItemStack seedStack = recipe.getSeed();
			if (seed.getItem() == seedStack.getItem()
					&& (seed.getMetadata() == seedStack.getMetadata()
							|| seedStack.getMetadata() == 32767)) {
				
				//土壌の判断
				boolean soilFlg = false;
				for (ItemStack soilStack : recipe.getSoilList()) {
					if (soil.getItem() == soilStack.getItem()
							&& (soil.getMetadata() == soilStack.getMetadata()
									|| soilStack.getMetadata() == 32767)) {
						soilFlg = true;
						break;
					}
				}
				//種と土壌が一致すれば問題なしと判断
				if (soilFlg) {
					return recipe;
				}
			}
		}
		return null;
	}
	
	//******************************************************************************************
	
	/**
	 * コンストラクタ(外部から直接呼び出さないようにする)
	 */
	private RecipesKitchenGarden(ItemStack seed, IBlockState seedState, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress, int minAge, int maxAge) {
		this.itemSeed = seed;
		this.stateSeed = seedState;
		this.itemSoilList = soilList;
		this.itemFertilizerList = fertilizerList;
		this.itemHarvestList = harvestList;
		this.progress = progress;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}
	
	/**
	 * 種
	 */
	protected ItemStack itemSeed;
	public ItemStack getSeed() {
		return itemSeed.copy();
	}
	
	/**
	 * 種描画用ブロック
	 */
	protected IBlockState stateSeed;
	
	/**
	 * 土壌
	 */
	protected List<ItemStack> itemSoilList;
	public List<ItemStack> getSoilList() {
		return this.itemSoilList;
	}
	
	/**
	 * 肥料
	 */
	protected List<ItemStack> itemFertilizerList;
	public List<ItemStack> getFertilizerList() {
		return this.itemFertilizerList;
	}
	
	/**
	 * 収穫物
	 */
	protected List<ItemStack> itemHarvestList;
	public List<ItemStack> getHarvestList() {
		return this.itemHarvestList;
	}
	
	/**
	 * 育成時間(tick)
	 */
	protected int progress;
	public int getProgress() {
		return this.progress;
	}
	
	/**
	 * 描画用のメタデータMin
	 */
	protected int minAge;
	
	/**
	 * 描画用のメタデータMax
	 */
	protected int maxAge;
	
	/**
	 * 進捗状態に応じた描画を返却する
	 * @param progress
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public IBlockState getSeedStateProgress(int progress) {
		
		int stage = maxAge - minAge + 1;
		
		int stageValue = (int) Math.floor((double)this.progress / (double)stage);
		
		int meta = progress / stageValue;
		meta = Math.min(meta, this.maxAge);
		
		return this.stateSeed.getBlock().getStateFromMeta(meta);
	}
	
	/**
	 * 肥料判定
	 * @param Fertilizer
	 * @return
	 */
	public boolean isFertilizer(ItemStack fertilizer) {
		boolean ret = false;
		
		if (fertilizer.isEmpty()) return ret;
		
		for (ItemStack stack : itemFertilizerList) {
		
			if (fertilizer.getItem() == stack.getItem()
					&& fertilizer.getMetadata() == stack.getMetadata()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}
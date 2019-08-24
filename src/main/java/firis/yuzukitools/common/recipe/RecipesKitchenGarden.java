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
    	//ネザーウォート
    	register(new ItemStack(Items.NETHER_WART), 
    			Blocks.NETHER_WART.getDefaultState(), 
    			SoilType.SOUL_SAND,
    			200,
    			0,
    			3,
    			new ItemStack(Items.NETHER_WART));
    	
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
    	
    	//カボチャ
    	register(new ItemStack(Items.PUMPKIN_SEEDS), 
    			Blocks.PUMPKIN.getDefaultState(), 
    			SoilType.DIRT,
    			200,
    			new ItemStack(Blocks.PUMPKIN));
    	
    	//スイカ
    	register(new ItemStack(Items.MELON_SEEDS), 
    			Blocks.MELON_BLOCK.getDefaultState(), 
    			SoilType.DIRT,
    			200,
    			new ItemStack(Blocks.MELON_BLOCK));
    	
    	//きのこ
    	register(new ItemStack(Blocks.RED_MUSHROOM), 
    			Blocks.RED_MUSHROOM.getDefaultState(), 
    			SoilType.MUSHROOM,
    			200,
    			new ItemStack(Blocks.RED_MUSHROOM));
    	register(new ItemStack(Blocks.BROWN_MUSHROOM), 
    			Blocks.BROWN_MUSHROOM.getDefaultState(), 
    			SoilType.MUSHROOM,
    			200,
    			new ItemStack(Blocks.BROWN_MUSHROOM));
    	
    	//オークの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 0), 
    			Blocks.SAPLING.getStateFromMeta(0), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 4, 0),
    			new ItemStack(Blocks.SAPLING, 1, 0),
    			new ItemStack(Items.APPLE, 1));
    	
    	//マツの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 1), 
    			Blocks.SAPLING.getStateFromMeta(1), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 1),
    			new ItemStack(Blocks.SAPLING, 1, 1));
    	
    	//シラカバの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 2), 
    			Blocks.SAPLING.getStateFromMeta(2), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 2),
    			new ItemStack(Blocks.SAPLING, 1, 2));
    	
    	//ジャングルの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 3), 
    			Blocks.SAPLING.getStateFromMeta(3), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG, 5, 3),
    			new ItemStack(Blocks.SAPLING, 1, 3));
    	
    	//アカシアの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 4), 
    			Blocks.SAPLING.getStateFromMeta(4), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG2, 5, 0),
    			new ItemStack(Blocks.SAPLING, 1, 4));
    	
    	//ダークオークの苗木
    	register(new ItemStack(Blocks.SAPLING, 1, 5), 
    			Blocks.SAPLING.getStateFromMeta(5), 
    			SoilType.DIRT,
    			400,
    			new ItemStack(Blocks.LOG2, 5, 1),
    			new ItemStack(Blocks.SAPLING, 1, 5));
    	
    	//お花
    	for (int meta = 0; meta <= 8; meta++) {
	    	register(new ItemStack(Blocks.RED_FLOWER, 1, meta), 
	    			Blocks.RED_FLOWER.getStateFromMeta(meta), 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.RED_FLOWER, 1, meta));
    	}
    	for (int meta = 0; meta <= 0; meta++) {
	    	register(new ItemStack(Blocks.YELLOW_FLOWER, 1, meta), 
	    			Blocks.YELLOW_FLOWER.getStateFromMeta(meta), 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.YELLOW_FLOWER, 1, meta));
    	}
    	
    	//コーラスフラワー
    	register(new ItemStack(Blocks.CHORUS_FLOWER, 1), 
    			Blocks.CHORUS_FLOWER.getDefaultState(), 
    			SoilType.END_STONE,
    			400,
    			new ItemStack(Blocks.CHORUS_FLOWER, 1),
    			new ItemStack(Items.CHORUS_FRUIT, 2));
    	
    	//2段の高さのお花
    	for (int meta = 0; meta <= 5; meta++) {
	    	register(new ItemStack(Blocks.DOUBLE_PLANT, 1, meta), 
	    			Blocks.DOUBLE_PLANT.getStateFromMeta(meta), 
	    			SoilType.DIRT,
	    			200,
	    			new ItemStack(Blocks.DOUBLE_PLANT, 1, meta));
    	}
    	
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
		if (cropItem != itemPlant) {
			harvestList.add(new ItemStack(cropItem, 1));
			harvestList.add(new ItemStack(itemPlant));
		} else {
			harvestList.add(new ItemStack(cropItem, 2));
		}

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
	 * 汎用登録用
	 * 骨粉対応
	 */
	public static void register(ItemStack seed, IBlockState seedState, SoilType soilType, int progress, ItemStack... harvest) {
		register(seed, seedState, soilType, progress, 0, 0, harvest);
	}
	
	/**
	 * 汎用登録用(metadata対応)
	 * 骨粉対応
	 */
	public static void register(ItemStack seed, IBlockState seedState, SoilType soilType, int progress, int minAge, int maxAge, ItemStack... harvest) {
		
		//レシピ設定用
		List<ItemStack> soilList = new ArrayList<ItemStack>();
		List<ItemStack> fertilizerList = new ArrayList<ItemStack>();
		List<ItemStack> harvestList = new ArrayList<ItemStack>();
		
		//土壌
		soilList = soilType.getSoilList();

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
				minAge,
				maxAge
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
	@SuppressWarnings("deprecation")
	private RecipesKitchenGarden(ItemStack seed, IBlockState seedState, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress, int minAge, int maxAge) {
		this.itemSeed = seed;
		this.itemSoilList = soilList;
		this.itemFertilizerList = fertilizerList;
		this.itemHarvestList = harvestList;
		this.progress = progress;

		//IBlockStateを設定する
		//minAgeとmaxAgeが0の場合はstateをそのまま設定する
		this.stateSeedList = new ArrayList<IBlockState>();
		if (minAge == 0 && maxAge == 0) {
			this.stateSeedList.add(seedState);
		} else {
			//Age分のメタデータを設定する
			for (int meta = minAge; meta <= maxAge; meta++) {
				this.stateSeedList.add(seedState.getBlock().getStateFromMeta(meta));
			}
		}
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
	protected List<IBlockState> stateSeedList;
	public List<IBlockState> getStateSeedList() {
		return this.stateSeedList;
	}
	
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
	 * 進捗状態に応じた描画を返却する
	 * @param progress
	 * @return
	 */
	public IBlockState getSeedStateProgress(int progress) {
		
		//minAgeとmaxAgeが0の場合はstateをそのまま返却する
		if (this.stateSeedList.size() == 1) {
			return this.stateSeedList.get(0);
		}
		
		int stage = this.stateSeedList.size();
		
		int stageValue = (int) Math.floor((double)this.progress / (double)stage);
		
		int meta = progress / stageValue;
		meta = Math.min(meta, this.stateSeedList.size() - 1);
		
		return this.stateSeedList.get(meta);
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

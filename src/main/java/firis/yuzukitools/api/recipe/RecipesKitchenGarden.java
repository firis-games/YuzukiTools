package firis.yuzukitools.api.recipe;

import java.util.List;

import firis.yuzukitools.api.YuzukiToolsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class RecipesKitchenGarden {
	
	/**
	 * レシピに一致するかの判断を行う
	 * @param stack
	 * @return
	 */
	public static RecipesKitchenGarden getRecipe(ItemStack seed, ItemStack soil) {
		
		if (seed.isEmpty() || soil.isEmpty()) return null;
		
		for(RecipesKitchenGarden recipe : YuzukiToolsAPI.kitchenGardenRecipes) {
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
	 * コンストラクタ
	 */
	public RecipesKitchenGarden(ItemStack seed, List<List<IBlockState>> seedStateList, List<ItemStack> soilList, List<ItemStack> fertilizerList, List<ItemStack> harvestList, int progress) {
		this.itemSeed = seed;
		this.itemSoilList = soilList;
		this.itemFertilizerList = fertilizerList;
		this.itemHarvestList = harvestList;
		this.progress = progress;
		this.stateSeedList = seedStateList;
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
	protected List<List<IBlockState>> stateSeedList;
	public List<List<IBlockState>> getStateSeedList() {
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
	public List<IBlockState> getSeedStateProgress(int progress) {
		
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
					&& fertilizer.getMetadata() == stack.getMetadata()
					&& fertilizer.getCount() >= stack.getCount()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 肥料使用個数
	 * @param Fertilizer
	 * @return
	 */
	public int shrinkFertilizerCount(ItemStack fertilizer) {
		int shrink = 0;
		if (fertilizer.isEmpty()) return shrink;
		
		for (ItemStack stack : itemFertilizerList) {
		
			if (fertilizer.getItem() == stack.getItem()
					&& fertilizer.getMetadata() == stack.getMetadata()
					&& fertilizer.getCount() >= stack.getCount()) {
				shrink = stack.getCount();
				break;
			}
		}
		return shrink;
	}
}

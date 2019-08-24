package firis.yuzukitools.common.recipe.json;

import java.util.ArrayList;
import java.util.List;

import firis.core.common.helper.JsonHelper;
import firis.yuzukitools.common.recipe.RecipesKitchenGarden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * 家庭菜園のレシピクラスとjsonを
 * @author computer
 *
 */
public class JsonRecipesKitchenGarden {
	
	/**
	 * レシピオブジェクトをjsonオブジェクトへ変換
	 * @param recipe
	 */
	public static JsonRecipesKitchenGarden toJson(RecipesKitchenGarden recipe) {
		JsonRecipesKitchenGarden json = new JsonRecipesKitchenGarden();
		
		//種
		json.seed = JsonHelper.toString(recipe.getSeed());
		
		//描画オブジェクト
		json.display = new ArrayList<List<String>>();
		for (List<IBlockState> stateList : recipe.getStateSeedList()) {
			List<String> strStateList = new ArrayList<String>();
			for (IBlockState state : stateList) {
				strStateList.add(JsonHelper.toString(state));
			}
			json.display.add(strStateList);
		}
		
		//土壌リスト
		json.soil = toStringList(recipe.getSoilList());
		
		//肥料リスト
		json.fertilizer = toStringList(recipe.getFertilizerList());
		
		//収穫物
		json.harvest = toStringList(recipe.getHarvestList());
		
		//育成速度
		json.progress = recipe.getProgress(); 

		
		return json;
	}
	
	/**
	 * ItemStackのリストを文字列のリストへ変換する
	 * @param stackList
	 * @return 
	 * @return 
	 */
	private static List<String> toStringList(List<ItemStack> stackList) {
		List<String> strList = new ArrayList<String>();
		for (ItemStack stack : stackList) {
			strList.add(JsonHelper.toString(stack));
		}
		return strList;
	}
	
	
	/**
	 * 種
	 */
	public String seed;
	
	/**
	 * 描画オブジェクト
	 */
	public List<List<String>> display;
	
	/**
	 * 土壌
	 */
	public List<String> soil;

	/**
	 * 肥料
	 */
	public List<String> fertilizer;
	
	/**
	 * 収穫アイテム
	 */
	public List<String> harvest;
	
	/**
	 * 育成時間
	 */
	public Integer progress;
	
}

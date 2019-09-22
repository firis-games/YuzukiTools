package firis.yuzukitools.common.instanthouse;

import java.util.ArrayList;
import java.util.List;

import firis.core.common.helper.JsonHelper;
import firis.core.common.helper.JsonHelper.JsonHelperException;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.template.Template;

public class InstantHouseInfo {

	/**
	 * 構造情報
	 */
	private Template tempate;
	public Template getTemplate() {
		return this.tempate;
	}
	
	/**
	 * アイコン用ItemStack
	 */
	private ItemStack stack = null;
	public ItemStack getIconItemStack() {
		
		//初アクセスの際に生成処理
		if (this.stack == null) {
			this.stack = ItemStack.EMPTY;
			if (jsonInfo != null) {
				try {
					this.stack = JsonHelper.fromStringItemStack(jsonInfo.item);
				} catch (JsonHelperException e) {
				}
			}
		}
		return this.stack;
	}
	
	/**
	 * アイテム表示名(モデル直接指定)
	 */
	private String item_model = "";
	public String getItemModel() {
		return this.item_model;
	}
	
	/**
	 * アイテム表示名
	 */
	private String display = "";
	public String getDisplay() {
		return this.display;
	}
	
	/**
	 * レシピリストを生成する
	 */
	public List<ItemStack> createRecipes() {
		List<ItemStack> recipes = new ArrayList<>();
		
		//初アクセスの際に生成処理
		if (jsonInfo != null) {
			for (String recipe : jsonInfo.recipes) {
				try {
					recipes.add(JsonHelper.fromStringItemStack(recipe));
				} catch (JsonHelperException e) {
				}
			}
		} else {
			//レシピの初期化
			if (autoReg != -1) {
				recipes.add(new ItemStack(YKItems.YUZUKI_MEDAL));
				recipes.add(new ItemStack(Items.BED, 1, 32767));
				recipes.add(new ItemStack(YKItems.YUZUKI_MEDAL));
				recipes.add(new ItemStack(Blocks.CRAFTING_TABLE));
				recipes.add(new ItemStack(Blocks.FURNACE));
				recipes.add(new ItemStack(Blocks.CHEST));
				recipes.add(new ItemStack(Items.DYE, 1, autoReg));
				recipes.add(new ItemStack(Items.DYE, 1, autoReg));
				recipes.add(new ItemStack(Items.DYE, 1, autoReg));
			} else {
				//通常版
				recipes.add(new ItemStack(Blocks.PLANKS, 1, 32767));
				recipes.add(new ItemStack(Blocks.PLANKS, 1, 32767));
				recipes.add(new ItemStack(Blocks.PLANKS, 1, 32767));
				recipes.add(new ItemStack(Blocks.PLANKS, 1, 32767));
				recipes.add(new ItemStack(YKItems.YUZUKI_MEDAL));
				recipes.add(new ItemStack(Blocks.PLANKS, 1, 32767));
				recipes.add(new ItemStack(Blocks.COBBLESTONE));
				recipes.add(new ItemStack(Blocks.COBBLESTONE));
				recipes.add(new ItemStack(Blocks.COBBLESTONE));
			}
		}
		return recipes;
	}
	
	private JsonInstantHouseInfo jsonInfo = null;
	
	/**
	 * 自動登録番号
	 */
	private int autoReg = -1;
	public int getAutoRegNo() {
		return this.autoReg;
	}
	
	/**
	 * コンストラクタ
	 * @param tempate
	 * @param jsonInfo
	 */
	public InstantHouseInfo(Template tempate, JsonInstantHouseInfo jsonInfo, int autoReg) {
		
		//Template設定
		this.tempate = tempate;
		
		if (jsonInfo != null) {
			this.display = jsonInfo.display;
			this.item_model = jsonInfo.item_model;
			
			//この段階ではItemとBlockのインスタンスが生成されていないため
			//json情報のまま保持する
			this.jsonInfo = jsonInfo;
			this.autoReg = -1;
			
		} else {
			this.autoReg = autoReg;
		}
	}
}

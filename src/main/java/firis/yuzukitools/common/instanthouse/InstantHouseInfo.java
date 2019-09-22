package firis.yuzukitools.common.instanthouse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import firis.core.common.helper.JsonHelper;
import firis.core.common.helper.JsonHelper.JsonHelperException;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	 * アイテム表示名
	 */
	private String display = "";
	public String getDisplay() {
		return this.display;
	}
	
	/**
	 * レシピ
	 */
	private List<ItemStack> recipes = null;
	public List<ItemStack> getRecipes() {
		//初アクセスの際に生成処理
		if (this.recipes == null) {
			this.recipes = new ArrayList<>();
			if (jsonInfo != null) {
				for (String recipe : jsonInfo.recipes) {
					try {
						this.recipes.add(JsonHelper.fromStringItemStack(recipe));
					} catch (JsonHelperException e) {
					}
				}
			} else {
				//レシピの初期化
				this.recipes.add(new ItemStack(YKItems.YUZUKI_MEDAL));
				this.recipes.add(new ItemStack(Blocks.CRAFTING_TABLE));
				this.recipes.add(new ItemStack(Blocks.FURNACE));
				this.recipes.add(new ItemStack(Items.BED, 1, 32767));
				this.recipes.add(new ItemStack(Blocks.CHEST));
			}
		}
		return this.recipes;
	}
	
	private JsonInstantHouseInfo jsonInfo = null;
	
	/**
	 * コンストラクタ
	 * @param tempate
	 * @param jsonInfo
	 */
	public InstantHouseInfo(Template tempate, JsonInstantHouseInfo jsonInfo) {
		
		//Template設定
		this.tempate = tempate;
		
		if (jsonInfo != null) {
			this.display = jsonInfo.display;
			
			//この段階ではItemとBlockのインスタンスが生成されていないため
			//json情報のまま保持する
			this.jsonInfo = jsonInfo;
		}
	}
	
	/**
	 * JsonInfoから必要な情報を設定する
	 * @param jsonInfo
	 * @throws JsonHelperException 
	 */
	public void fromJson(@Nonnull JsonInstantHouseInfo jsonInfo) throws JsonHelperException {
		
		if (jsonInfo == null) throw new JsonHelperException("fromStringItemStack");
		
		//アイテム設定
		this.stack = JsonHelper.fromStringItemStack(jsonInfo.item);
		
		//表示名設定
		this.display = jsonInfo.display;
		
		//レシピ
		this.recipes = new ArrayList<>();
		for (String recipe : jsonInfo.recipes) {
			this.recipes.add(JsonHelper.fromStringItemStack(recipe));
		}
	}
	
	/**
	 * 標準レシピを設定する
	 */
	public void initDefault() {
		this.stack = ItemStack.EMPTY;
		this.display = "";
		this.recipes = new ArrayList<>();
		
		//このタイミングだとまだ
		//YKItems.YUZUKI_MEDALにインスタンスが格納されていない
		
		//デフォルトレシピ
		this.recipes.add(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(YuzukiTools.MODID, "yuzuki_medal"))));
		this.recipes.add(new ItemStack(Blocks.CRAFTING_TABLE));
		this.recipes.add(new ItemStack(Blocks.FURNACE));
		this.recipes.add(new ItemStack(Blocks.BED));
		this.recipes.add(new ItemStack(Blocks.CHEST));
		
	}
	
}

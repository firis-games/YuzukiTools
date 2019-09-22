package firis.yuzukitools.common.instanthouse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import firis.core.common.helper.JsonHelper;
import firis.core.common.helper.JsonHelper.JsonHelperException;
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
	private ItemStack stack;
	public ItemStack getIconItemStack() {
		return this.stack;
	}
	
	/**
	 * アイテム表示名
	 */
	private String display;
	public String getDisplay() {
		return this.display;
	}
	
	/**
	 * レシピ
	 */
	private List<ItemStack> recipes;
	
	/**
	 * コンストラクタ
	 * @param tempate
	 * @param jsonInfo
	 */
	public InstantHouseInfo(Template tempate, JsonInstantHouseInfo jsonInfo) {
		
		//Template設定
		this.tempate = tempate;
		
		try {
			this.fromJson(jsonInfo);
		} catch (JsonHelperException e) {
			//Json変換を失敗した場合はデフォルト設定する
			initDefault();
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
	}
	
}

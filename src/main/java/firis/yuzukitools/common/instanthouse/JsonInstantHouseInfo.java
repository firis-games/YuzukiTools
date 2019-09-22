package firis.yuzukitools.common.instanthouse;

import java.util.ArrayList;
import java.util.List;

/**
 * インスタントハウスNBTのjsonフォーマット
 * @author computer
 *
 */
public class JsonInstantHouseInfo {
	
	/**
	 * 表示用アイコン設定
	 */
	public String item = "";
	
	/**
	 * 表示用アイコン設定モデル用
	 */
	public String item_model = "";
	
	/**
	 * 表示名
	 */
	public String display = "";
	
	/**
	 * レシピ
	 */
	public List<String> recipes = new ArrayList<>();
	
}

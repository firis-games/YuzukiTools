package firis.yuzukitools.api.recipe;

public interface IYuzukiToolsPlugin {

	/**
	 * 家庭菜園登録処理
	 * @param subtypeRegistry
	 */
	default void registerKitchenGardenRecipe(KitchenGardenRegistry recipe) {
	}
	
}

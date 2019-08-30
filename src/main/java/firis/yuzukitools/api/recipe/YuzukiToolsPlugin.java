package firis.yuzukitools.api.recipe;

/**
 * YuzukiToolsレシピ登録用アノテーション
 * @author computer
 *
 */
public @interface YuzukiToolsPlugin {
	
	String modid() default "";
	
}

package firis.yuzukitools.common.config;

import firis.yuzukitools.YuzukiTools;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = YuzukiTools.MODID, type = Type.INSTANCE, name = YuzukiTools.MODID)
public class YKConfig {
	/**
	 * Jsonレシピを読み込む
	 */
	@Comment({"use Json Recipe", "default:false"})
	public static boolean USE_JSON_RECIPE = false;
	
	/**
	 * 自動連携機能
	 */
	@Comment({"use Kitchen Garden Auto Register", "default:false"})
	public static boolean USE_KITCHEN_GARDEN_AUTO_REGISTER = false;
	
	/**
	 * レシピ出力機能
	 */
	@Comment({"Developers Tools output json Kitchen Garden Json Recipe", "default:false"})
	public static boolean DEVELOP_TOOL_OUTPUT_JSON_KITCHEN_GARDEN_RECIPE = false;
}

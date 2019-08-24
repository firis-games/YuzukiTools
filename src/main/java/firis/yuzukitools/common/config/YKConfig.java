package firis.yuzukitools.common.config;

import firis.yuzukitools.YuzukiTools;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = YuzukiTools.MODID, type = Type.INSTANCE, name = YuzukiTools.MODID)
public class YKConfig {
	/**
	 * レシピ登録方法にJson形式を使う
	 */
	@Comment({"use Json Recipe", "default:false"})
	public static boolean USE_JSON_RECIPE = false;
}

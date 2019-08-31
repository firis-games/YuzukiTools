package firis.yuzukitools.api.recipe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * YuzukiToolsレシピ登録用アノテーション
 * @author computer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface YuzukiToolsPlugin {
	
	/** ModId */
	String modid() default "";
	
	/** 優先度 */
	EventPriority priority() default EventPriority.NORMAL;
	
}

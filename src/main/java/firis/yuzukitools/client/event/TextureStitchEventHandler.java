package firis.yuzukitools.client.event;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class TextureStitchEventHandler {

	@SubscribeEvent
	public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		
		event.getMap().registerSprite(
				new ResourceLocation(YuzukiTools.MODID, "blocks/energy"));
		
	}
	
}

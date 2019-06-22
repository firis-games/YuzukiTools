package firis.yuzukitools.client.proxy;

import java.util.Map;

import firis.yuzukitools.client.event.KeyBindingHandler;
import firis.yuzukitools.client.layer.YKBackPackLayer;
import firis.yuzukitools.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class ClientProxy implements IProxy {

	@Override
	public void registerKeyBinding() {
		KeyBindingHandler.init();
	}

	@Override
	public void initLayerRenderer() {
		
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		
		RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new YKBackPackLayer());

		render = skinMap.get("slim");
		render.addLayer(new YKBackPackLayer());
		
	}
	
	
	
}

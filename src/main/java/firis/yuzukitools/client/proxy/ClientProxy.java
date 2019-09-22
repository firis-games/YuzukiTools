package firis.yuzukitools.client.proxy;

import java.util.Map;

import firis.yuzukitools.client.event.JetpackClientTickEventHandler;
import firis.yuzukitools.client.event.KeyBindingHandler;
import firis.yuzukitools.client.event.ModelBakeEventInstantHouseHandler;
import firis.yuzukitools.client.event.TextureStitchEventHandler;
import firis.yuzukitools.client.layer.YKBackPackLayer;
import firis.yuzukitools.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IProxy {

	@Override
	public void registerEvent() {
		//ClientOnlyイベント登録
		MinecraftForge.EVENT_BUS.register(JetpackClientTickEventHandler.class);
		MinecraftForge.EVENT_BUS.register(KeyBindingHandler.class);
		MinecraftForge.EVENT_BUS.register(TextureStitchEventHandler.class);
		MinecraftForge.EVENT_BUS.register(ModelBakeEventInstantHouseHandler.class);
	}
	
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

	@Override
	public void spawnParticle(BlockPos pos, int spawnType) {
		World world = Minecraft.getMinecraft().world;
		
		//骨粉パーティクルを生成
		if (spawnType == 0) {
			int amount = 7;
			for (int i = 0; i < amount; i++) {
	            double d0 = world.rand.nextGaussian() * 1.5D + 5D;
	            double d1 = world.rand.nextGaussian() * 1.5D + 5D;
	            double d2 = world.rand.nextGaussian() * 1.5D + 5D;
	            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, 
	            		(double)((float)pos.getX() + world.rand.nextFloat()),
	            		(double)((float)pos.getY() + world.rand.nextFloat()),
	            		(double)((float)pos.getZ() + world.rand.nextFloat()), 
	            		d0, d1, d2);
				
			}
		}
	}
}

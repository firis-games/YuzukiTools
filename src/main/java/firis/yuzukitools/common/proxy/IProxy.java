package firis.yuzukitools.common.proxy;

import net.minecraft.util.math.BlockPos;

public interface IProxy {

	public void registerEvent();
	
	public void registerKeyBinding();
	
	public void initLayerRenderer();
	
	public void spawnParticle(BlockPos pos, int spawnNo);
	
}

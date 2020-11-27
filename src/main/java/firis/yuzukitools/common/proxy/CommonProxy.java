package firis.yuzukitools.common.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class CommonProxy implements IProxy {

	@Override
	public void registerEvent() {}
	
	@Override
	public void registerKeyBinding() {}

	@Override
	public void initLayerRenderer() {}

	@Override
	public void spawnParticle(BlockPos pos, int spawnNo) {}

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

}

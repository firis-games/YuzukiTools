package firis.yuzukitools.mobbottle.common.block;

import firis.mobbottle.common.block.FBlockMobBottle;
import firis.yuzukitools.mobbottle.common.tileentity.FTileEntityMobBottlePlus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FBlockMobBottlePlus extends FBlockMobBottle {

	
	
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new FTileEntityMobBottlePlus();
	}
	
}

package firis.yuzukitools.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 手持ち作業台
 * @author firis-games
 *
 */
public class YKContainerPortableWorkbench extends ContainerWorkbench {

	public YKContainerPortableWorkbench(InventoryPlayer playerInventory, World worldIn) {
		super(playerInventory, worldIn, BlockPos.ORIGIN);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}

package firis.yuzukitools.client.gui;

import firis.yuzukitools.common.container.YKContainerPortableWorkbench;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

/**
 * 手持ち作業台
 * @author firis-games
 *
 */
public class YKGuiPortableWorkbench extends GuiCrafting {

	public YKGuiPortableWorkbench(InventoryPlayer playerInv, World worldIn) {
		super(playerInv, worldIn);
		this.inventorySlots = new YKContainerPortableWorkbench(playerInv, worldIn);
	}

}

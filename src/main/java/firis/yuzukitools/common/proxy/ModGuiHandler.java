package firis.yuzukitools.common.proxy;

import firis.core.common.inventory.CapabilityInventory;
import firis.yuzukitools.client.gui.YKGuiContainerBackpack;
import firis.yuzukitools.common.container.YKContainerBackpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	//バックパック
	public final static int BACKPACK_BLOCK = 0;
	
	//バックパック(アイテム)
	public final static int BACKPACK_ITEM = 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory inventory = null;
		EnumHand hand = null;
		
		switch (ID) {
		//バックパック
		case ModGuiHandler.BACKPACK_BLOCK :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKContainerBackpack(inventory, player.inventory);
		
		//バックパック(アイテム)
		case ModGuiHandler.BACKPACK_ITEM :
			hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			inventory = new CapabilityInventory(player.getHeldItem(hand));
			return new YKContainerBackpack(inventory, player.inventory, y);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		IInventory inventory = null;
		EnumHand hand = null;
		
		switch (ID) {
		//バックパック
		case ModGuiHandler.BACKPACK_BLOCK :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKGuiContainerBackpack(inventory, player.inventory);
			
		//バックパック(アイテム)
		case ModGuiHandler.BACKPACK_ITEM :
			hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			inventory = new CapabilityInventory(player.getHeldItem(hand));
			return new YKGuiContainerBackpack(inventory, player.inventory, y);
		}
		
		return null;
	}
}

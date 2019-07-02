package firis.yuzukitools.common.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class YKContainerSolarCharger extends AbstractContainer {

	public YKContainerSolarCharger(IInventory inventory, InventoryPlayer playerInv) {
		super(inventory, playerInv);
	}

	@Override
	protected void initInventorySlot(IInventory inventory) {
		
		//ソーラー触媒
		this.addSlotToContainer(new Slot(inventory, 0, 30, 60) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getItem() == Items.REDSTONE;
		    }
		});

		//放電スロット
		this.addSlotToContainer(new Slot(inventory, 1, 128, 26) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getCapability(CapabilityEnergy.ENERGY, null) != null;
		    }
		});
		
		//充電スロット
		this.addSlotToContainer(new Slot(inventory, 2, 128, 60) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getCapability(CapabilityEnergy.ENERGY, null) != null;
		    }
		});
		
	}

	@Override
	protected void initPlayerInventorySlot(InventoryPlayer playerInv) {
		this.initPlayerInventorySlotStandard(playerInv, 8, 84);
	}
	
}

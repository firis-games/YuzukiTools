package firis.yuzukitools.common.container;

import firis.yuzukitools.common.tileentity.YKTileKitchenGarden;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class YKContainerKitchenGarden extends AbstractContainer {

	public YKContainerKitchenGarden(IInventory inventory, InventoryPlayer playerInv) {
		super(inventory, playerInv);
	}

	@Override
	protected void initInventorySlot(IInventory inventory) {
		
		//放電/放水
		this.addSlotToContainer(new Slot(inventory, 
				YKTileKitchenGarden.Slot.Discharge, 8, 17) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getCapability(CapabilityEnergy.ENERGY, null) != null 
		        		|| stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) != null ;
		    }
		});
		
		//充電/給水
		this.addSlotToContainer(new Slot(inventory, 
				YKTileKitchenGarden.Slot.Charge, 8, 53) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getCapability(CapabilityEnergy.ENERGY, null) != null
		        	|| stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) != null ;
		    }
		});
				
		//種
		this.addSlotToContainer(new Slot(inventory, 
				YKTileKitchenGarden.Slot.Seed, 56, 21) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
		});
		
		//土壌
		this.addSlotToContainer(new Slot(inventory, 
				YKTileKitchenGarden.Slot.Soil, 56, 50) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
		});
		
		//肥料
		this.addSlotToContainer(new Slot(inventory, 
				YKTileKitchenGarden.Slot.Fertilizer, 85, 54));
		
		//収穫物スロット
		int xBasePos = 110;
		int yBasePos = 18;
		int invX = 3;
		int invY = 3;
		int baseSlot = 5;
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new Slot(inventory, slotIndex, xPos, yPos));
            }
        }
	}

	@Override
	protected void initPlayerInventorySlot(InventoryPlayer playerInv) {
		this.initPlayerInventorySlotStandard(playerInv, 8, 84);
	}
	
}

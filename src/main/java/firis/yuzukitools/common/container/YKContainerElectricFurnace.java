package firis.yuzukitools.common.container;

import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class YKContainerElectricFurnace extends AbstractContainer {

	public YKContainerElectricFurnace(IInventory inventory, InventoryPlayer playerInv) {
		super(inventory, playerInv);
	}

	@Override
	protected void initInventorySlot(IInventory inventory) {
		
		//材料
		this.addSlotToContainer(new Slot(inventory, 0, 52, 35) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return RecipesElectricFurnace.getRecipe(stack) != null;
		    }
		});
		
		//結果
		this.addSlotToContainer(new Slot(inventory, 1, 116, 35) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return false;
		    }
		});

		//放電スロット
		this.addSlotToContainer(new Slot(inventory, 2, 8, 17) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
		        return stack.getCapability(CapabilityEnergy.ENERGY, null) != null;
		    }
		});
		
		//充電スロット
		this.addSlotToContainer(new Slot(inventory, 3, 8, 53) {
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

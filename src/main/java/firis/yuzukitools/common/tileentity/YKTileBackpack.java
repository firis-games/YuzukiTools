package firis.yuzukitools.common.tileentity;

import javax.annotation.Nullable;

import firis.yuzukitools.common.capability.TileEntityItemStackHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;

public class YKTileBackpack extends AbstractTileEntity {

	public TileEntityItemStackHandler inventory;
	
	public YKTileBackpack() {
		
		//Inventory初期化
		this.inventory = new TileEntityItemStackHandler(54);
		
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//ItemHandler
		inventory.deserializeNBT(compound);
		
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //ItemHandler
        compound.merge(inventory.serializeNBT());
        
        return compound;
    }
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
    }

	@Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
		}
    	return super.getCapability(capability, facing);
    
    }
}

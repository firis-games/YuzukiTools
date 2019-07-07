package firis.yuzukitools.common.capability;

import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityEnergyStorage extends EnergyStorage {

	public TileEntityEnergyStorage(TileEntity tile, int capacity) {
		super(capacity);
		this.tile = tile;
	}
	
	private TileEntity tile;
	
	/**
	 * EnergyStorageをNBT化
	 * @return
	 */
    public NBTTagCompound serializeNBT() {
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setInteger("energy", this.energy);
    	nbt.setInteger("capacity", this.capacity);
    	nbt.setInteger("maxReceive", this.maxReceive);
    	nbt.setInteger("maxExtract", this.maxExtract);
        return nbt;
    }

    /**
     * NBTからEnergyStorageへ反映
     * @param nbt
     */
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.energy = nbt.getInteger("energy");
    	this.capacity = nbt.getInteger("capacity");
    	this.maxReceive = nbt.getInteger("maxReceive");
    	this.maxExtract = nbt.getInteger("maxExtract");
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
    	int ret = super.receiveEnergy(maxReceive, simulate);
    	if (!simulate) {
    		VanillaNetworkHelper.sendPacketTileEntity(this.tile);
    	}
    	return ret;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
    	int ret = super.extractEnergy(maxExtract, simulate);
    	if (!simulate) {
    		VanillaNetworkHelper.sendPacketTileEntity(this.tile);
    	}
    	return ret;
    }

}

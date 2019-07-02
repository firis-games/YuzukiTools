package firis.yuzukitools.common.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityEnergyStorage extends EnergyStorage {

	public TileEntityEnergyStorage(int capacity) {
		super(capacity);
	}
	
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

}

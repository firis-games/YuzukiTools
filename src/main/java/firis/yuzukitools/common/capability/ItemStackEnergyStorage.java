package firis.yuzukitools.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemStackEnergyStorage implements IEnergyStorage {

	protected ItemStack stack;
	
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    
    protected final String NBT_ENERGY = "energy";
    
	public ItemStackEnergyStorage(int capacity, ItemStack stack) {
		this.stack = stack;
		
		this.capacity = capacity;
		this.maxReceive = capacity;
		this.maxExtract = capacity;

		this.init();
	}
	
	/**
	 * 初期化
	 */
	public void init() {
		
		//ItemStackが空またはエネルギー用のタグがない場合は初期化
		if (!this.stack.hasTagCompound() || 
				! this.stack.getTagCompound().hasKey(NBT_ENERGY)) {
			setEnergy(0);
		}
	}
	
	
	@Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
    	int energy = getEnergyStored();
    	
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        
        setEnergy(energy);
        return energyReceived;
    }
    

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
    	int energy = getEnergyStored();
    	
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        
        setEnergy(energy);
        return energyExtracted;
    }
    
	@Override
    public int getEnergyStored()
    {
    	int energy = 0;
    	if (stack.hasTagCompound()) {
    		NBTTagCompound nbt = stack.getTagCompound();
    		energy = nbt.getInteger(NBT_ENERGY);
    	}
    	return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }
    
    /**
     * NBT
     * @param energy
     */
    private void setEnergy(int energy) {
    	NBTTagCompound nbt = new NBTTagCompound();
    	
    	if (stack.hasTagCompound()) {
    		nbt = stack.getTagCompound();
    	}
    	nbt.setInteger(NBT_ENERGY, energy);
    	stack.setTagCompound(nbt);
    }

}

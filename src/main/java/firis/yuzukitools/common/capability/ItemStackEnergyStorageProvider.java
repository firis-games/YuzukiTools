package firis.yuzukitools.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * ItemStack用IEnergyStorageProvaider
 * @author computer
 *
 */
public class ItemStackEnergyStorageProvider implements ICapabilityProvider {

	private final IEnergyStorage energyStorage;

	/**
	 * コンストラクタ
	 */
	public ItemStackEnergyStorageProvider(int capacity, ItemStack stack) {
		this.energyStorage = new ItemStackEnergyStorage(capacity, stack);
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		else return null;
	}
}
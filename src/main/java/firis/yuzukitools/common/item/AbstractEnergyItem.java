package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractEnergyItem extends Item {
	
	protected int capacity;

	/**
	 * コンストラクタ
	 */
	public AbstractEnergyItem(int capacity) {

		this.capacity = capacity;

		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setMaxStackSize(1);
	}
	
	/**
	 * エネルギーを表示
	 */
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		//エネルギーをとりあえず表示する
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return;
		
		Integer fe = capability.getEnergyStored();
		Integer maxFe = capability.getMaxEnergyStored();
		tooltip.add(fe.toString() + "FE / " + maxFe.toString() + "FE");
    }
	
	/**
	 * アイテムゲージの表示
	 */
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
	
	/**
	 * アイテムゲージをエネルギーとリンクさせる
	 */
	@Override
    public double getDurabilityForDisplay(ItemStack stack) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return 0.0D;
		
		return 1.0D - (double)capability.getEnergyStored() / (double)capability.getMaxEnergyStored();
    }
	
	
	/**
	 * エネルギー残量チェック
	 * @return
	 */
	protected boolean isEnergyStored(ItemStack stack) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return false;
		
		if (capability.getEnergyStored() == 0) return false;
		
		return true;
	}
	
	/**
	 * エネルギーを消費する
	 * @param stack
	 */
	protected void extractEnergy(ItemStack stack, int energy) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return;
		
		capability.extractEnergy(energy, false);
	}
	
    /**
     * ForgeEnergyCapability制御用
     */
    @Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ForgeEnergyProvider(this.capacity);
    }
    
    /**
     * ForgeEnergyCapability制御用クラス
     */
	private static class ForgeEnergyProvider implements ICapabilitySerializable<NBTBase> {

		private final IEnergyStorage energyStorage;

		/**
		 * コンストラクタ
		 */
		public ForgeEnergyProvider(int capacity) {
			this.energyStorage = new EnergyStorage(capacity);
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

		@Override
		public NBTBase serializeNBT() {
			return CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			CapabilityEnergy.ENERGY.readNBT(energyStorage, null, nbt);
		}
	}	
}
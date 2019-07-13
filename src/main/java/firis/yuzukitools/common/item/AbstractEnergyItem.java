package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.capability.ItemStackEnergyStorageProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractEnergyItem extends Item {
	
	protected int capacity;
	
	/**
	 * ツール1回あたりの消費量
	 */
	public static int USE_ENERGY = 80;

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
		
		Integer energy = capability.getEnergyStored();
		Integer maxEnergy = capability.getMaxEnergyStored();
		String battery = I18n.format("info.energy_battery.name");
		String unit = I18n.format("info.energy_unit.name");
		
		tooltip.add(battery + " " 
				+ TextFormatting.LIGHT_PURPLE + energy.toString() + "/" + maxEnergy.toString() + " " + unit);
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
	 * エンチャント可否
	 */
	@Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
	
    /**
     * ForgeEnergyCapability制御用
     */
    @Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ItemStackEnergyStorageProvider(this.capacity, stack);
    }
}

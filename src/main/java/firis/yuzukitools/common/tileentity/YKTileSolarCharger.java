package firis.yuzukitools.common.tileentity;

import javax.annotation.Nullable;

import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class YKTileSolarCharger extends AbstractTileEntity implements ITickable {
	
	/**	
	 * Inventory管理用
	 */
	public ItemStackHandler inventory;
	
	/**
	 * Energy管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * 
	 */
	public YKTileSolarCharger() {

		//Inventory
		this.inventory = new ItemStackHandler(3);
		
		//Energy
		this.energy = new TileEntityEnergyStorage(100000);
		
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//Inventory
		this.inventory.deserializeNBT(compound);

		//Energy
		this.energy.deserializeNBT(compound);
		
		//redstonePower
		this.redstonePower = compound.getInteger("redstonePower");

    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //ItemHandler
        compound.merge(this.inventory.serializeNBT());

        //Energy
        compound.merge(this.energy.serializeNBT());

        //redstonePower
        compound.setInteger("redstonePower", this.redstonePower);
        
        return compound;
    }
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityEnergy.ENERGY) {
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
		} else if(capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
    	return super.getCapability(capability, facing);
    
    }

	private int tick = 0;
	
	/**
	 * Tick処理
	 */
	@Override
	public void update() {
		
		tick++;

		//ソーラー発電
		this.updateSolarCharger();
		
		//ツール放電
		this.updateToolDischarge();
		
		//ツール充電
		this.updateToolCharge();
		
		//同期
		if (tick % 10 == 0) {
			VanillaNetworkHelper.sendPacketTileEntity(this);
		}
		
	}
	
	/**
	 * ツール充電
	 */
	public void updateToolDischarge() {
		
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(1);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;
		
		moveEnergy(stackStorage, this.energy);
	}
	
	/**
	 * ツール充電
	 */
	public void updateToolCharge() {
		
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(2);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;
		
		moveEnergy(this.energy, stackStorage);

	}
	
	/**
	 * FromからToへエネルギーを移動する
	 * @param fromEnergy
	 * @param toEnergy
	 */
	protected void moveEnergy(IEnergyStorage fromEnergy, IEnergyStorage toEnergy) {
		
		//満充電または充電出来ない場合は何もしない
		if(toEnergy.getMaxEnergyStored() <= toEnergy.getEnergyStored()
				|| !toEnergy.canReceive()) return;
		
		//最大チャージ数
		int charge = toEnergy.getMaxEnergyStored() - toEnergy.getEnergyStored();
		charge = Math.min(charge, 50000);
		charge = Math.min(charge, fromEnergy.getEnergyStored());
		
		//シミュレート
		charge = toEnergy.receiveEnergy(charge, true);
		
		//実行
		toEnergy.receiveEnergy(charge, false);
		fromEnergy.extractEnergy(charge, false);
				
	}
	
	protected int redstonePower = 0;
	public boolean isSolarRedstonePower() {
		return redstonePower > 0;
	}
	
	/**
	 * ソーラー発電処理
	 * 80FE/t
	 */
	public void updateSolarCharger() {
		
		int bTick = 2;
		
		//bTickに1回処理を行う
		if (tick % bTick != 0) return;
		
		//満充電の場合はなにもしない
		if (this.energy.getMaxEnergyStored() <= this.energy.getEnergyStored()) return;
		
		//光判定 or 時間判定
		if (!isSolarChargerActive()) return;
		
		//レッドストーン活性化
		avtiveRedstonePower(bTick);
		
		//発電(10tickあたり40FE)
		this.energy.receiveEnergy(4 * bTick, false);
		
		//レッドストーン活性化分(10tickあたり160FE)
		if (redstonePower > 0) {
			this.energy.receiveEnergy(16 * bTick, false);
			redstonePower--;
			//レッドストーン活性化
			avtiveRedstonePower(bTick);
		}
	}
	
	/**
	 * ソーラー発電が稼動可能かのチェックを行う
	 */
	public boolean isSolarChargerActive() {
		//天気 ：雨
		if (this.getWorld().getWorldInfo().isRaining()) {
			return false;
		}
		//天気 ：雷雨
		if (this.getWorld().getWorldInfo().isThundering()) {
			return false;
		}
		//空の見える場所
		if(!this.getWorld().canSeeSky(this.getPos().up())) {
			return false;
		}
		
		long wolrdTime = this.getWorld().getWorldTime() % 24000;
		//時間の判定
		//06:00-18:00を昼間と判断する
		if (0 <= wolrdTime 
				&& wolrdTime <= 12000) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * レッドストーンを消費してRSソーラーモード起動
	 * @param bTick
	 */
	private void avtiveRedstonePower(int bTick) {
		//レッドストーン活性化
		if (redstonePower == 0) {
			ItemStack stack = this.inventory.getStackInSlot(0);
			//レッドストーンの場合消費して
			if (!stack.isEmpty() && stack.getItem() == Items.REDSTONE && stack.getCount() > 0) {
				//40秒分ブースト(800tick)
				redstonePower = 800 / bTick;
				stack.shrink(1);
			}
		}
	}

}

package firis.yuzukitools.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools.YKBlocks;
import firis.yuzukitools.common.block.YKBlockElectricFurnace;
import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.capability.TileEntityItemStackHandler;
import firis.yuzukitools.common.helpler.EnergyHelper;
import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

public class YKTileElectricFurnace extends AbstractTileEntity implements ITickable {

	/**	
	 * Inventory管理用
	 */
	public TileEntityItemStackHandler inventory;
	
	/**
	 * Energy管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * コンストラクタ
	 */
	public YKTileElectricFurnace() {

		//Inventory
		this.inventory = new TileEntityItemStackHandler(4);
		this.inventory.setInputSlot(new ArrayList<Integer>(Arrays.asList(0)));
		this.inventory.setOutputSlot(new ArrayList<Integer>(Arrays.asList(1)));
		
		//Energy
		this.energy = new TileEntityEnergyStorage(this, 20000);
		
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
		this.burnTime = compound.getInteger("burnTime");
		this.maxBurnTime = compound.getInteger("maxBurnTime");

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

        //燃焼時間
        compound.setInteger("burnTime", this.burnTime);
        compound.setInteger("maxBurnTime", this.maxBurnTime);
        
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

	@Override
	public void update() {

		//隣接するバッテリーからエネルギー取得
		updateBatteryCharge();
		
		//ツール放電
		updateToolDischarge();
		
		//ツール充電
		updateToolCharge();
		
		//電気炉
		updateFurnace();
		
	}
	
	/**
	 * 隣接するバッテリーからエネルギー取得
	 */
	public void updateBatteryCharge() {
		
		for (EnumFacing facing : EnumFacing.VALUES) {

			//充電済みの場合は何もしない
			if(this.energy.getMaxEnergyStored() <= this.energy.getEnergyStored()) break;
			
			TileEntity tile = this.getWorld().getTileEntity(this.pos.offset(facing));

			//自身でない
			if (tile == null || tile instanceof YKTileElectricFurnace) continue;
			
			//方角を反転してCapability取得
			IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
			if (storage == null) continue;
			
			//エネルギーの移動
			EnergyHelper.moveEnergy(storage, this.energy, 50000);
		}
		
	}
	
	
	/**
	 * ツール放電
	 */
	public void updateToolDischarge() {
		
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(2);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;

		EnergyHelper.moveEnergy(stackStorage, this.energy, 50000);
	}
	
	/**
	 * ツール充電
	 */
	public void updateToolCharge() {
		
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(3);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;
		
		EnergyHelper.moveEnergy(this.energy, stackStorage, 50000);

	}
	
	/**
	 * 1tickあたりの消費エネルギー
	 */
	public final static int ENERGY_UNIT = 40;
	
	/**
	 * 燃焼時間
	 */
	private int burnTime = 0;
	
	/**
	 * 最大燃焼時間
	 */
	private int maxBurnTime = 0;
	
	public double getProgress() {
		if (maxBurnTime == 0) return 0.0D;
		return (double) burnTime / (double) maxBurnTime;
	}
	
	/**
	 * 電気炉処理
	 */
	public void updateFurnace() {
		
		//ブロックのメタデータ設定
		setBlockElectricFurnace();
		
		//動いてなければ何もしない
		if (!isActive()) return;
		
		//燃焼処理
		burnTime++;
		this.energy.extractEnergy(ENERGY_UNIT, false);
		
		//出力結果
		if (burnTime < maxBurnTime) return;
		
		//出力結果処理を行う
		ItemStack stack = RecipesElectricFurnace.getOutputItemStack(this.inventory.getStackInSlot(0));
		
		this.inventory.directExtractItem(0, 1, false);
		this.inventory.directInsertItem(1, stack, false);
		
		this.burnTime = 0;
		
		//同期
		VanillaNetworkHelper.sendPacketTileEntity(this);
		
	}
	
	/**
	 * 電気炉の稼動状態の有無を判断する
	 * @return
	 */
	public boolean isActive() {
		
		boolean outputSlot = false;
		int recipeBurnTime = 0;
		int energy = this.energy.getEnergyStored();

		RecipesElectricFurnace recipe = RecipesElectricFurnace.getRecipe(this.inventory.getStackInSlot(0));
		if (recipe != null) {
			recipeBurnTime = recipe.getBurnTime();
			
			//outputslotのチェック
			ItemStack simStack = this.inventory.directInsertItem(1, recipe.getOutputItemStack(), true);
			if (simStack.isEmpty()) {
				outputSlot = true;
			}
		}
		
		//inputスロットにアイテムあり
		if (recipeBurnTime > 0 
				&& energy > ENERGY_UNIT
				&& outputSlot) {
			this.maxBurnTime = recipeBurnTime;
			return true;
		}
		
		if (recipeBurnTime == 0) {
			this.burnTime = 0;
			this.maxBurnTime = 0;
		}
		
		return false;
	}
	
	/**
	 * 状態に応じてメタデータの置き換えを行う
	 */
	protected void setBlockElectricFurnace() {
		
		IBlockState state = this.getWorld().getBlockState(this.getPos());
		
		boolean active = state.getValue(YKBlockElectricFurnace.ACTIVE);
		
		if (active == isActive()) return;
		
		//違う場合は置き換える
		TileEntity tile = this.getWorld().getTileEntity(this.getPos());
		
		//モード切替
		IBlockState newState = YKBlocks.ELECTRIC_FURNACE.getDefaultState()
			.withProperty(YKBlockElectricFurnace.ACTIVE, !active)
			.withProperty(YKBlockElectricFurnace.FACING, state.getValue(YKBlockElectricFurnace.FACING));
		this.getWorld().setBlockState(this.getPos(), newState, 3);
		
		if (tile != null) {
			tile.validate();
			this.getWorld().setTileEntity(this.getPos(), tile);
		}
		
	}
	
}

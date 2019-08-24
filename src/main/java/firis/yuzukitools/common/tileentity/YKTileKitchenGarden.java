package firis.yuzukitools.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.capability.TileEntityFluidHandler;
import firis.yuzukitools.common.capability.TileEntityItemStackHandler;
import firis.yuzukitools.common.helpler.EnergyHelper;
import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import firis.yuzukitools.common.recipe.RecipesKitchenGarden;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;

public class YKTileKitchenGarden extends AbstractTileEntity implements ITickable {

	/**
	 * スロット定数
	 */
	public static class Slot {
		public static int Seed = 0;
		public static int Soil = 1;
		public static int Fertilizer = 2;
		public static int Discharge = 3;
		public static int Charge = 4;
	}
	
	
	public static int PROGRESS_ENERGY = 5;
	public static int PROGRESS_WATER = 1;
	
	/**	
	 * Inventory管理用
	 */
	public TileEntityItemStackHandler inventory;
	
	/**
	 * Energy管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * 液体管理用
	 */
	public TileEntityFluidHandler fluid;
	
	/**
	 * コンストラクタ
	 */
	public YKTileKitchenGarden() {
		
		//Inventory
		this.inventory = new TileEntityItemStackHandler(14);
		this.inventory.setInputSlot(new ArrayList<Integer>(Arrays.asList(Slot.Fertilizer)));
		this.inventory.setOutputSlot(new ArrayList<Integer>(Arrays.asList(5, 6, 7, 8, 9, 10, 11, 12, 13)));
		
		//Energy
		this.energy = new TileEntityEnergyStorage(this, 10000);
		
		//Fluid
		this.fluid = new TileEntityFluidHandler(FluidRegistry.getFluid("water"), this, 5000);
		
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
		
		//Fluid
		this.fluid.deserializeNBT(compound);
		
		//progress
		this.progress = compound.getInteger("progress");
		
		//レシピの更新
		this.recipesKitchenGarden = RecipesKitchenGarden.getRecipe(
				this.inventory.getStackInSlot(Slot.Seed),
				this.inventory.getStackInSlot(Slot.Soil));
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
        
        //Fluid
        compound.merge(this.fluid.serializeNBT());

		//progress
        compound.setInteger("progress", this.progress);

        return compound;
    }
	
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == CapabilityEnergy.ENERGY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
		} else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.fluid);
		}
    	return super.getCapability(capability, facing);
    
    }
	
	/**
	 * ゲージ描画用
	 * @return
	 */
	public double getGageProgress() {
		if (this.getMaxProgress() == 0) return 0;
		return (double)this.progress / (double)this.getMaxProgress();
	}
	
	protected int progress;
	public int getProgress() {
		return this.progress;
	}
	public int getMaxProgress() {
		return this.recipesKitchenGarden == null ? 0 : this.recipesKitchenGarden.getProgress();
	}
	
	int tick = 0;
	
	@Override
	public void update() {
		
		if (this.getWorld().isRemote) return;
		
		//隣接するバッテリー
		updateBatteryCharge();
		//放電
		updateEnergyDischarge();
		//充電
		updateEnergyCharge();
		//放水
		updateFluidDischarge();
		//給水
		updateFluidCharge();
		
		//家庭菜園のメイン処理
		updateKitchenGarden();
		
		//同期
		VanillaNetworkHelper.sendPacketTileEntity(this);
		
	}
	
	protected RecipesKitchenGarden recipesKitchenGarden;
	
	/**
	 * 描画用種
	 * @return
	 */
	public List<IBlockState> getRenderStateSeed() {
		if (this.recipesKitchenGarden != null) {
			return this.recipesKitchenGarden.getSeedStateProgress(this.progress);
		}
		return null;
	}
	/**
	 * 描画用土壌
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public IBlockState getRenderStateSoil() {
		ItemStack stack = this.inventory.getStackInSlot(Slot.Soil);
		
		if (!stack.isEmpty()) {
			
			Block itemBlock = Block.getBlockFromItem(stack.getItem());
			if (itemBlock != null) {
				
				//土の場合は耕地に変換する
				if (Blocks.DIRT == itemBlock && stack.getMetadata() == 0) {
					return Blocks.FARMLAND.getDefaultState().withProperty(
							BlockFarmland.MOISTURE, Integer.valueOf(7));
				}
				
				IBlockState state = itemBlock.getStateFromMeta(stack.getMetadata());
				return state;
			}
		}
		return null;
	}
	
	/**
	 * 家庭菜園のメイン処理
	 */
	public void updateKitchenGarden() {
		
		//レシピチェック
		RecipesKitchenGarden chkRecipesKitchenGarden = RecipesKitchenGarden.getRecipe(
				this.inventory.getStackInSlot(Slot.Seed),
				this.inventory.getStackInSlot(Slot.Soil));
		
		//レシピが対象外になった場合はリセット
		if(chkRecipesKitchenGarden == null) {
			this.recipesKitchenGarden = null;
			this.progress = 0;
			return;
		}
		//切り替えられた場合進捗をリセット
		if (recipesKitchenGarden == null || (chkRecipesKitchenGarden.getSeed().getItem() != recipesKitchenGarden.getSeed().getItem()
						|| chkRecipesKitchenGarden.getSeed().getMetadata() != recipesKitchenGarden.getSeed().getMetadata())) {
			recipesKitchenGarden = chkRecipesKitchenGarden;
			this.progress = 0;
		}
		
		//肥料判定による強制育成
		if (this.progress < this.getMaxProgress()) {
			//肥料判定
			if (this.recipesKitchenGarden.isFertilizer(
					this.inventory.getStackInSlot(Slot.Fertilizer))) {
				//1tickあたりの消費量
				int tickEnergy = PROGRESS_ENERGY * 50;
				//1tickあたりの水消費量
				int tickWater = PROGRESS_WATER * 50;
				//エネルギーor水が足りない場合
				FluidStack water = this.fluid.drain(tickWater, false);
				if(this.energy.extractEnergy(tickEnergy, true) != tickEnergy
						|| water == null
						|| water.amount != tickWater) {
				} else {
					//問題なければ消費して進捗を進める
					this.inventory.directExtractItem(Slot.Fertilizer, 1, false);
					this.fluid.drain(tickWater, true);
					this.energy.extractEnergy(tickEnergy, false);
					this.progress = this.getMaxProgress();
				}
			} else {
				//肥料がない場合は通常処理
				
				//1tickあたりの消費量
				int tickEnergy = PROGRESS_ENERGY;
				//1tickあたりの水消費量
				int tickWater = PROGRESS_WATER;
				//エネルギーor水が足りない場合
				FluidStack water = this.fluid.drain(tickWater, false);
				if(this.energy.extractEnergy(tickEnergy, true) != tickEnergy
						|| water == null
						|| water.amount != tickWater) {
					return;
				}
				//問題なければ消費して進捗を進める
				this.fluid.drain(tickWater, true);
				this.energy.extractEnergy(tickEnergy, false);
				this.progress++;
			}
		}
		//progressが終わっている場合はoutputする
		if (this.progress >= this.getMaxProgress()) {
			
			//移動の処理
			List<ItemStack> harvestList = this.recipesKitchenGarden.getHarvestList();
			
			//すべてのアイテムが移動できるかシミュレート
			boolean simulate = true;
			for (ItemStack harvest : harvestList) {
				harvest = harvest.copy();
				//Outputスロットの分をループ
				for (int slot = 5; slot < 5 + 9; slot++) {
					harvest = this.inventory.directInsertItem(slot, harvest, true);
					if (harvest.isEmpty()) {
						break;
					}
				}
				if (!harvest.isEmpty()) {
					simulate = false;
				}
			}
			
			//収穫物を挿入できない場合は処理を中断
			if (simulate == false) return;
			
			//アイテムを挿入
			for (ItemStack harvest : harvestList) {
				harvest = harvest.copy();
				//Outputスロットの分をループ
				for (int slot = 5; slot < 5 + 9; slot++) {
					harvest = this.inventory.directInsertItem(slot, harvest, false);
					if (harvest.isEmpty()) {
						break;
					}
				}
			}
			
			//進捗をリセット
			this.progress = 0;
		}
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
			if (tile == null || tile instanceof YKTileKitchenGarden) continue;
			
			//方角を反転してCapability取得
			IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
			if (storage == null) continue;
			
			//エネルギーの移動
			EnergyHelper.moveEnergy(storage, this.energy, 50000);
		}
	}
	
	/**
	 * 放電
	 */
	public void updateEnergyDischarge() {
		
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(Slot.Discharge);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;

		EnergyHelper.moveEnergy(stackStorage, this.energy, 10000);
	}
	
	/**
	 * 充電
	 */
	public void updateEnergyCharge() {
		//電気ツール取得
		ItemStack stack = this.inventory.getStackInSlot(Slot.Charge);
		if (stack.isEmpty()) return;
		IEnergyStorage stackStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (stackStorage == null) return;
		
		EnergyHelper.moveEnergy(this.energy, stackStorage, 10000);
	}
	
	/**
	 * 放水
	 */
	public void updateFluidDischarge() {
		
		//液体容器取得
		ItemStack stack = this.inventory.getStackInSlot(Slot.Discharge);
		if (stack.isEmpty()) return;
		IFluidHandlerItem stackFluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (stackFluidHandler == null) return;
		
		//液体の空き容量
		int emptyLiquid = this.fluid.getMaxLiquid() - this.fluid.getLiquid();
		
		//バケツから液体を取得
		FluidStack stackFluidStack = stackFluidHandler.drain(emptyLiquid, false);
		if (stackFluidStack == null) {
			return;
		}
		
		//バケツの液体を容器へ移動
		int moveliquid = this.fluid.fill(stackFluidStack, false);
		
		//0の場合は何もしない
		if (moveliquid == 0) {
			return;
		}
		
		//問題なければ移動処理
		stackFluidStack = stackFluidHandler.drain(moveliquid, true);
		this.fluid.fill(stackFluidStack, true);

		//空き容器
		ItemStack emptyStack = stackFluidHandler.getContainer().copy();
		
		this.inventory.setStackInSlot(Slot.Discharge, emptyStack);
		
	}
	
	/**
	 * 給水
	 */
	public void updateFluidCharge() {
		
		//液体容器取得
		ItemStack stack = this.inventory.getStackInSlot(Slot.Charge);
		if (stack.isEmpty()) return;
		IFluidHandlerItem stackFluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		
		//nullは処理しない
		if (stackFluidHandler == null) return;
		
		//1回あたりの移動量
		int moveLiquid = 1000;
		
		//タンクから液体取得
		if(!this.fluid.canDrain()) return;
		FluidStack tankFluidStack = this.fluid.drain(moveLiquid, false);
		if (tankFluidStack == null) return;
		
		//タンクの中身をバケツへ移動
		moveLiquid = stackFluidHandler.fill(tankFluidStack, false);
				
		//0の場合は何もしない
		if (moveLiquid == 0) return;
		
		//実際に処理を行う
		tankFluidStack = this.fluid.drain(moveLiquid, true);
		moveLiquid = stackFluidHandler.fill(tankFluidStack, true);
		
		//空き容器
		ItemStack fillStack = stackFluidHandler.getContainer().copy();
		
		this.inventory.setStackInSlot(Slot.Charge, fillStack);
		
	}

}

package firis.yuzukitools.common.capability;

import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * 単一液体管理用FluidHandler
 * @author computer
 *
 */
public class TileEntityFluidHandler implements IFluidHandler, IFluidTankProperties {
	
	/**
	 * コンストラクタ
	 */
	public TileEntityFluidHandler(Fluid fluidType, TileEntity tile, int maxLiquid) {
		this.liquid = 0;
		this.maxLiquid = maxLiquid;
		this.fluidType = fluidType;
		this.tile = tile;
	}
	
	protected final TileEntity tile;
	
	/************************************************
	 * 液体管理用
	 *************************************************/
	protected final Fluid fluidType;

	protected Integer liquid = 0;
	public Integer getLiquid() {
		return this.liquid;
	}
	public void receiveLiquid(Integer liquid) {
		this.liquid = this.liquid + liquid;
		//同期処理
		VanillaNetworkHelper.sendPacketTileEntity(this.tile);
	}
	
	/**
	 * 
	 */
	protected Integer maxLiquid = 0;
	public Integer getMaxLiquid() {
		return this.maxLiquid;
	}
	
	/************************************************
	 * 液体保存用
	 *************************************************/
    public NBTTagCompound serializeNBT(){
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("liquid", this.liquid);
        nbt.setInteger("maxLiquid", this.maxLiquid);
        return nbt;
    }
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.liquid = nbt.getInteger("liquid");
        this.maxLiquid = nbt.getInteger("maxLiquid");
    }
	
	/************************************************
	 * IFluidHandler
	 * capabilityで受け渡すための処理
	 *************************************************/
	protected IFluidTankProperties[] tankProperties;
	
	/**
	 * @Intarface IFluidHandler
	 */
	@Override
    public IFluidTankProperties[] getTankProperties()
    {
        if (this.tankProperties == null)
        {
            this.tankProperties = new IFluidTankProperties[] {this};
        }
        return this.tankProperties;
    }

	/**
	 * @Intarface IFluidHandler
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		
		//対象外
		if (!canFillFluidType(resource) 
				|| resource == null 
				|| resource.amount <= 0) {
            return 0;
		}
        
		//シミュレート
    	if (!doFill) {
    		return Math.min(this.getMaxLiquid() - this.getLiquid(), resource.amount);
    	}
    	
    	//実際に処理を行う
    	int filled = this.getMaxLiquid() - this.getLiquid();

    	//最大値より受け入れる値が低い場合はそのまま加算
        if (resource.amount < filled) {
            this.receiveLiquid(resource.amount);
            filled = resource.amount;
        } else {
        	this.receiveLiquid(filled);
        }

        /* IFluidTankを継承している場合は呼ばれる
        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(
        		this.getContents(), 
        		this.getWorld(), 
        		this.getPos(), 
        		this, 
        		filled));
        */
        return filled;
	}
	
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		
		if (!canFillFluidType(resource)) {
			return null;
		}
		return this.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		
		//空の場合
		if (this.getLiquid() <= 0 || maxDrain <= 0) {
            return null;
        }
		
		int drained = maxDrain;
        if (this.getLiquid() < drained) {
            drained = this.getLiquid();
        }

        //返却用の流体Stack
        FluidStack stack = new FluidStack(this.fluidType, drained);

        //シミュレート
        if (doDrain) {
        	//液体操作
        	this.receiveLiquid(-drained);
            /* IFluidTankを継承している場合は呼ばれる
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
            */
        }
        
        return stack;
	}
	
	/************************************************
	 * IFluidTankProperties
	 * IFluidHandlerで必要なインタフェース
	 *************************************************/
	@Override
	public FluidStack getContents() {
		FluidStack contents = new FluidStack(fluidType, this.getLiquid());
        return contents == null ? null : contents.copy();
	}

	@Override
	public int getCapacity() {
		 return this.getMaxLiquid();
	}

	/**
	 * 入力可否
	 */
	@Override
	public boolean canFill() {
		return true;
	}

	/**
	 * 出力可否
	 */
	@Override
	public boolean canDrain() {
		return true;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		//同じ液体か判断する
		return fluidStack.isFluidEqual(this.getContents()) && canFill();
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
		//同じ液体か判断する
		return fluidStack.isFluidEqual(this.getContents()) && canDrain();
	}
	
}

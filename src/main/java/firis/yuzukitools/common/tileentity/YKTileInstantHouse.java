package firis.yuzukitools.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class YKTileInstantHouse extends AbstractTileEntity {

	
	protected EnumFacing facing = EnumFacing.NORTH;
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}
	public EnumFacing getFacing() {
		return this.facing;
	}
	
	/**
	 * 描画範囲を設定する
	 */
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		
		int range = 15;
		
        return new AxisAlignedBB(pos.add(-range, -1, -range), pos.add(range, 10, range));
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("facing", this.facing.getHorizontalIndex());

        return compound;
    }
	
}

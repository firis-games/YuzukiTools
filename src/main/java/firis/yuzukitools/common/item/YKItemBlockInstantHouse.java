package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools.YKBlocks;
import firis.yuzukitools.common.tileentity.YKTileInstantHouse;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * インスタントハウスアイテム
 * @author computer
 *
 */
public class YKItemBlockInstantHouse extends ItemBlock {

	/**
	 * コンストラクタ
	 */
	public YKItemBlockInstantHouse() {
		super(YKBlocks.INSTANT_HOUSE);
		this.setMaxStackSize(1);
	}
	
	
	/**
	 * ブロック設置後にInventoryの内容を設定
	 */
	@Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
		boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

		//NBTからTemplate情報を書き込み
		String templete = "";
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("template")) {
			templete = stack.getTagCompound().getString("template");
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof YKTileInstantHouse) {
			YKTileInstantHouse house = (YKTileInstantHouse) tile;
			house.setTemplate(templete);
		}
		
		return ret;
		
    }
	
	/**
	 * Creativeタブ登録
	 */
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
        	//ノーマルブロック
        	ItemStack stack = new ItemStack(this);
        	NBTTagCompound nbt = new NBTTagCompound();
        	nbt.setString("template", "house/ykt_house");
        	stack.setTagCompound(nbt);        	
            items.add(stack);
        }
    }

}

package firis.yuzukitools.common.block;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import firis.yuzukitools.common.tileentity.YKTileSolarCharger;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class YKBlockSolarCharger extends BlockContainer {

	/**
	 * コンストラクタ
	 */
	public YKBlockSolarCharger() {
		
		super(Material.PISTON);
		this.setHardness(0.1F);
		this.setResistance(20.0F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileSolarCharger();
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (!worldIn.isRemote) {
	    	//右クリックでGUIを開く
			playerIn.openGui(YuzukiTools.INSTANCE, ModGuiHandler.SOLAR_CHARGER,
					worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
    	return true;
    }
	
	/**
	 * ブロック破壊時のイベント
	 */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null) {
        	IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        	if (capability != null) {
        		for (int i = 0; i < capability.getSlots(); i++) {
        			ItemStack stack = capability.getStackInSlot(i);
        			if (!stack.isEmpty()) {
        				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
        			}
        		}
        		worldIn.updateComparatorOutputLevel(pos, this);
        	}
        }
        super.breakBlock(worldIn, pos, state);
    }

}

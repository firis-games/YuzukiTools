package firis.yuzukitools.common.block;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import firis.yuzukitools.common.tileentity.YKTileBackpack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class YKBlockBackpack extends BlockContainer {

	/**
	 * 方角
	 */
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	
    protected final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(
    		0.0D / 16.0D, 0.0D / 16.0D, 4.0D / 16.0D, 
    		16.0D / 16.0D, 12.0D / 16.0D, 12.0D / 16.0D);

    protected final AxisAlignedBB BLOCK_AABB_SIDE = new AxisAlignedBB(
    		4.0D / 16.0D, 0.0D / 16.0D, 0.0D / 16.0D, 
    		12.0D / 16.0D, 12.0D / 16.0D, 16.0D / 16.0D);

	public YKBlockBackpack() {
		super(Material.PISTON);
		this.setHardness(0.1F);
		this.setResistance(20.0F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setSoundType(SoundType.CLOTH);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		EnumFacing facing = (EnumFacing) state.getProperties().get(FACING);
		
		if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			return BLOCK_AABB;
		} else {
			return BLOCK_AABB_SIDE;
		}
    }
	
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
		return true;
    }
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBackpack();
	}
	
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (!worldIn.isRemote) {
	    	//右クリックでGUIを開く
			playerIn.openGui(YuzukiTools.INSTANCE, ModGuiHandler.BACKPACK_BLOCK,
					worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
    	return true;
    }
	
	
	/**
	 * Drop制御
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		
		ItemStack drop = dropBlockWithInventory(this, worldIn, pos);
		spawnAsEntity(worldIn, pos, drop);
		
		super.breakBlock(worldIn, pos, state);
	}
	
	/**
	 * 標準Drop制御を無効化
	 */
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		
	}
	
	/**
	 * BlockからinventoryNBTをもつDrop用アイテムを生成する
	 * @param block
	 * @param world
	 * @param pos
	 */
	public static ItemStack dropBlockWithInventory(Block block, World world, BlockPos pos) {
		
		ItemStack stack = new ItemStack(Item.getItemFromBlock(block));
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			
			ItemStackHandler itemCapability = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStackHandler tileCapability = (ItemStackHandler) tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			
			NBTTagCompound nbt = tileCapability.serializeNBT();
			itemCapability.deserializeNBT(nbt);
			
		}		
		return stack;
	}
		
	/**
	 * 方角ブロック対応
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

}

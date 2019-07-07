package firis.yuzukitools.common.block;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import firis.yuzukitools.common.tileentity.YKTileElectricFurnace;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKBlockElectricFurnace extends AbstractBlockContainer {


	/**
	 * 電気炉起動状態
	 */
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	/**
	 * 方角
	 */
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	/**
	 * コンストラクタ
	 */
	public YKBlockElectricFurnace() {
		
		super(Material.PISTON);
		this.setHardness(0.1F);
		this.setResistance(20.0F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(ACTIVE, false)
				.withProperty(FACING, EnumFacing.NORTH));
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileElectricFurnace();
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (!worldIn.isRemote) {
	    	//右クリックでGUIを開く
			playerIn.openGui(YuzukiTools.INSTANCE, ModGuiHandler.ELECTRIC_FURNACE,
					worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
    	return true;
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {ACTIVE, FACING});
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
	public int getMetaFromState(IBlockState state){
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot){
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
}

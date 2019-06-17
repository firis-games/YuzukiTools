package firis.yuzukitools.common.block;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.tileentity.YKTileInstantHouse;
import firis.yuzukitools.common.world.generator.WorldGenHouse;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKBlockInstantHouse extends BlockContainer {

	public YKBlockInstantHouse() {
		super(Material.PISTON);
		this.setHardness(0.1F);
		this.setResistance(20.0F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileInstantHouse();
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		
		//方角を設定する
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof YKTileInstantHouse) {
			YKTileInstantHouse tile = (YKTileInstantHouse) tileEntity;
			tile.setFacing(placer.getHorizontalFacing());
		}
    }
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (!playerIn.isSneaking()
				|| !(tileEntity instanceof YKTileInstantHouse)) {
			return false;
		}
		
		YKTileInstantHouse tile = (YKTileInstantHouse) tileEntity;
		
		if (!worldIn.isRemote) {
			
			//インスタントハウスを生成する
			WorldGenHouse gen = new WorldGenHouse(tile.getFacing());
			boolean ret = gen.generate(worldIn, worldIn.rand, pos.down());
			//自身を消去
			if (ret) {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
		}
		
    	return true;
    }
	
}

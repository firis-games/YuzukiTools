package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKItemRedstoneShovel extends AbstractEnergyItemTool {

	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemRedstoneShovel(ToolMaterial material) {
	
		super(material, ItemSpade.EFFECTIVE_ON, 3000);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);

		this.setHarvestLevel("shovel", material.getHarvestLevel());

		this.attackDamage = 1.5F;
        this.attackSpeed = -3.0F;

	}
	
	/**
	 * ブロック破壊スピード制御
	 */
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		
		float defaultSpeed = 1.0F;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultSpeed;
		
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
	
	
    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        //エネルギーがない場合
      	if(!this.isEnergyStored(itemstack)) return EnumActionResult.PASS;
      		
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS)
            {
                IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, iblockstate1, 11);
                }
                
                //エネルギー消費
                this.extractEnergy(itemstack, this.useEnergy);

                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.PASS;
            }
        }
    }
	
}

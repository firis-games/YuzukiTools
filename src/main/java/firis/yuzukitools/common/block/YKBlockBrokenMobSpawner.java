package firis.yuzukitools.common.block;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 壊れたモブスポナー
 * @author firis-games
 *
 */
public class YKBlockBrokenMobSpawner extends Block {
	
	/**
	 * コンストラクタ
	 */
	public YKBlockBrokenMobSpawner()
	{
		super(Material.ROCK);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	/**
	 * スポーンエッグによる置き換え処理
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (worldIn.isRemote) {
			return true;
		}
		//メインハンドのアイテムがスポーンエッグの場合
		ItemStack handStack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		if (hand == EnumHand.MAIN_HAND 
				&& !handStack.isEmpty() 
				&& handStack.getItem() instanceof ItemMonsterPlacer) {
			
			//スポナーブロックへ置き換え
			worldIn.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityMobSpawner)
			{
				//スポーンエッグから中身を差し替え
				MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
				mobspawnerbaselogic.setEntityId(ItemMonsterPlacer.getNamedIdFrom(handStack));
			}
			tileentity.markDirty();
			worldIn.notifyBlockUpdate(pos, state, Blocks.MOB_SPAWNER.getDefaultState(), 3);
		
			if (!playerIn.capabilities.isCreativeMode)
			{
				handStack.shrink(1);
			}
			return true;
			
		}
		return false;
    }
	
	/**
	 * info設定
	 */
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.broken_mob_spawner.info"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("tile.broken_mob_spawner.details"));
    }

}

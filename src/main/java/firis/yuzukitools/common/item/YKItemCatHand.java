package firis.yuzukitools.common.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 猫の手
 * 
 * @author firis-games
 *
 */
public class YKItemCatHand extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemCatHand() {
		
		this.setMaxStackSize(1);
		
		//耐久度設定
		this.setMaxDamage(1280);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setFull3D();

		// 猫の手モード切り替え
		this.addPropertyOverride(new ResourceLocation("flatmode"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return YKItemCatHand.isFlatMode(stack) ? 1.0F : 0.0F;
			}
		});
	}

	/**
	 * モード変更
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		ItemStack handStack = playerIn.getHeldItem(handIn);
		
		if (!playerIn.isSneaking()) return new ActionResult<ItemStack>(EnumActionResult.PASS, handStack);

		NBTTagCompound nbt = new NBTTagCompound();
		if (handStack.hasTagCompound()) {
			nbt = handStack.getTagCompound();
		}

		if (isFlatMode(handStack)) {
			// 横置きモードへ変更
			nbt.setBoolean("flatmode", false);
		} else {
			// 平置きモードへ変更
			nbt.setBoolean("flatmode", true);
		}

		handStack.setTagCompound(nbt);

		// 鳴き声
		if (!worldIn.isRemote) {
			worldIn.playSound((EntityPlayer) null, playerIn.getPosition(), SoundEvents.ENTITY_CAT_AMBIENT,
					SoundCategory.AMBIENT, 0.75F, 1.0F);
			// パーティクル表示
			worldIn.playEvent(2005, playerIn.getPosition().up(), 0);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, handStack);
	}

	/**
	 * ブロック右クリック
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		return this.putBlockCatHand(player, worldIn, pos, hand, facing);
	}

	/**
	 * 猫の手設置処理
	 * 
	 * @param player
	 * @param world
	 * @param pos
	 * @param hand
	 * @param facing
	 * @return
	 */
	protected EnumActionResult putBlockCatHand(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing) {

		ItemStack handStack = player.getHeldItem(hand);

		// 横置きモード
		List<BlockPos> putPosList = new ArrayList<>();

		if (isFlatMode(handStack)) {
			putPosList = this.putFlatMode(player, world, pos, hand, facing);
		} else {
			putPosList = this.putHorizontalMode(player, world, pos, hand, facing);
		}
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		int meta = state.getBlock().damageDropped(state);
		ItemStack putStack = new ItemStack(block, 1, meta);

		boolean isCreative = player.capabilities.isCreativeMode;

		// ブロック設置
		boolean isPut = false;
		for (BlockPos putPos : putPosList) {

			// クリエイティブでない場合手持ちのアイテムを確認する
			ItemStack invStack = ItemStack.EMPTY;
			if (!isCreative) {
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack chkStack = player.inventory.getStackInSlot(i);
					// 設置アイテムと手持ちアイテムが一致する場合
					if (ItemStack.areItemsEqual(putStack, chkStack)) {
						invStack = chkStack;
						break;
					}
				}
			}

			// 対象が存在する場合に設置する
			if (!invStack.isEmpty() || isCreative) {
				isPut = true;
				world.setBlockState(putPos, state, 2);
				if (!world.isRemote) {
					world.playEvent(2005, putPos.up(), 0);
				}
				// アイテム消費
				if (!isCreative) {
					handStack.damageItem(1, player);
					invStack.shrink(1);
				}
			}
		}
		
		// 設置対象がある場合は鳴き声
		if (isPut && !world.isRemote) {
			world.playSound((EntityPlayer) null, player.getPosition(), SoundEvents.ENTITY_CAT_AMBIENT,
					SoundCategory.AMBIENT, 0.75F, 1.0F);
		}

		return EnumActionResult.SUCCESS;
	}

	/**
	 * 横置きモード 横方向へのブロックを設置していく
	 * 
	 * @param player
	 * @param world
	 * @param pos
	 * @param hand
	 * @param facing
	 */
	protected List<BlockPos> putHorizontalMode(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing) {

		List<BlockPos> resultPosList = new ArrayList<>();

		
		if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			facing = player.getHorizontalFacing().getOpposite();
		}
		
		//縦置き判定
		if (player.isSneaking()) {
			if (facing != EnumFacing.UP) {
				facing = EnumFacing.DOWN;
			} else {
				facing = EnumFacing.UP;
			}
			
		// 横置きモードはup/downはplayerの向いている方向
		} else if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			facing = player.getHorizontalFacing().getOpposite();
		}

		// ブロック設置補助
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		int meta = state.getBlock().getMetaFromState(state);

		// 設置位置サーチ(48マス)
		for (int i = 1; i <= 48; i++) {
			BlockPos chkPos = pos.offset(facing.getOpposite(), i);
			IBlockState chkState = world.getBlockState(chkPos);
			// 設置可能か
			if (chkState.getBlock().isReplaceable(world, chkPos)) {
				resultPosList.add(chkPos);
				break;
			}
			// 同じブロックか
			if (chkState.getBlock() != block && chkState.getBlock().getMetaFromState(chkState) != meta) {
				// 処理中断
				break;
			}
		}
		return resultPosList;
	}

	/**
	 * 平置きモード
	 * 
	 * @param player
	 * @param world
	 * @param pos
	 * @param hand
	 * @param facing
	 */
	protected List<BlockPos> putFlatMode(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing) {

		List<BlockPos> resultPosList = new ArrayList<>();

		// チェック対象のBlockPos
		List<BlockPos> chkPosList = new ArrayList<>();
		if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			// 平面置き
			chkPosList.add(pos.north());
			chkPosList.add(pos.north().east());
			chkPosList.add(pos.east());
			chkPosList.add(pos.east().south());
			chkPosList.add(pos.south());
			chkPosList.add(pos.south().west());
			chkPosList.add(pos.west());
			chkPosList.add(pos.west().north());
		} else {
			// 壁置き
			chkPosList.add(pos.up());
			chkPosList.add(pos.up().offset(facing.rotateY()));
			chkPosList.add(pos.up().offset(facing.rotateY().getOpposite()));
			chkPosList.add(pos.offset(facing.rotateY()));
			chkPosList.add(pos.offset(facing.rotateY().getOpposite()));
			chkPosList.add(pos.down());
			chkPosList.add(pos.down().offset(facing.rotateY()));
			chkPosList.add(pos.down().offset(facing.rotateY().getOpposite()));
		}

		// 設置可能かどうか
		for (BlockPos chkPos : chkPosList) {
			IBlockState chkState = world.getBlockState(chkPos);
			// 設置可能か
			if (chkState.getBlock().isReplaceable(world, chkPos)) {
				resultPosList.add(chkPos);
			}
		}
		return resultPosList;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		if (isFlatMode(stack)) {
			//平置きモード
			tooltip.add(TextFormatting.AQUA + I18n.format("item.cat_hand.info.flat"));
		} else {
			//横置きモード
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.cat_hand.info.normal"));
		}
		
    }
	
	/**
	 * NBTタグからモードを取得する
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean isFlatMode(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return false;
		}
		if (stack.getTagCompound().hasKey("flatmode") && stack.getTagCompound().getBoolean("flatmode")) {
			return true;
		}
		return false;
	}

}

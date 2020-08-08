package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKBlocks;
import firis.yuzukitools.common.api.baubles.BaublesHelper;
import firis.yuzukitools.common.capability.TileEntityItemStackHandler;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@Optional.Interface(modid="baubles", iface="baubles.api.IBauble")
public class YKItemBlockBackpack extends ItemBlock implements IBauble {

	/**
	 * アイテムのバックパック
	 */
	public YKItemBlockBackpack() {
		super(YKBlocks.BACKPACK);
		this.setMaxStackSize(1);
	}
	

	/**
	 * アーマースロットへ装備できるかの判断
	 * @return
	 */
    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
    	//Baublesが導入されている場合は無効化
    	if (BaublesHelper.isLoaded()) return false;
        return EntityEquipmentSlot.CHEST == armorType;
    }
    
	/**
	 * アーマー判断
	 * @return
	 */
    @SideOnly(Side.CLIENT)
    public EntityEquipmentSlot getEquipmentSlot()
    {
    	//Baublesが導入されている場合は無効化
    	if (BaublesHelper.isLoaded()) return null;
        return EntityEquipmentSlot.CHEST;
    }
    
    
    /**
     * ブロック設置orGUIを開く
     * スニークした場合のみブロックを設置する
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	    	
    	if (!player.isSneaking()) {
    		//GUIをオープンする
    		if (openGui(player, player.getHeldItem(hand), hand)) {
        		return EnumActionResult.SUCCESS;
    		} else {
        		return EnumActionResult.FAIL;
    		}
    	}
    	//スニーク時のみブロック設置処理を行う
    	return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    /**
     * GUIを開く
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	if (!playerIn.isSneaking()) {
    		//GUIをオープンする
    		if (openGui(playerIn, playerIn.getHeldItem(handIn), handIn)) {
    			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    		} else {
        		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    		}
    	}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
    
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.backpack.info"));
    }
    
    /**
     * バックパックGUI表示処理
     */
    public static boolean openGui(EntityPlayer player, ItemStack stack, EnumHand hand) {
    	
    	boolean ret = false;
    	
		if (!stack.isEmpty() && stack.getItem() instanceof YKItemBlockBackpack) {
			ret = true;
			
			int gui_id = ModGuiHandler.BACKPACK_ITEM;
			
			int handMode = hand == EnumHand.MAIN_HAND ? 1 : 2;
			
			int lockSlot = -1;
			if (EnumHand.MAIN_HAND == hand) {
				//HotBarのみ取得する
				for (int i = 0; i < 9; i++) {
					//完全一致のみ取得する
					if (player.inventory.getStackInSlot(i) == stack) {
						lockSlot = i;
						break;
					}
				}
			}
			
			if (lockSlot == -1) {
				gui_id = ModGuiHandler.BACKPACK_KEY;
			}
			
			player.openGui(YuzukiTools.INSTANCE, gui_id,
					player.getEntityWorld(), handMode, lockSlot, 0);
		}
		return ret;
    }
    
    /**
     * InventoryCapability制御用
     */
    @Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new InventoryProvider();
    }
    
    /**
     * InventoryCapability制御用クラス
     */
	private static class InventoryProvider implements ICapabilitySerializable<NBTBase> {

		private final TileEntityItemStackHandler inv = new TileEntityItemStackHandler(54);

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
			else return null;
		}

		@Override
		public NBTBase serializeNBT() {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
		}
	}
	
	/**
	 * ブロック設置後にInventoryの内容を設定
	 */
	@Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
		boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		setTileEntityCapability(world, player, pos, stack);
		return ret;
		
    }
	
	public static boolean setTileEntityCapability(World worldIn, @Nullable EntityPlayer player, BlockPos pos, ItemStack stack)
    {
		//チェックと必要なものを取得
        MinecraftServer minecraftserver = worldIn.getMinecraftServer();
        if (minecraftserver == null) return false;
    	ItemStackHandler itemCapability = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    	if (itemCapability == null) return false;
    	TileEntity tileentity = worldIn.getTileEntity(pos);
    	if (tileentity == null) return false;
    	ItemStackHandler tileCapability = (ItemStackHandler) tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    	if (tileCapability == null) return false;
    	
    	//IItemHandler上書き
    	NBTTagCompound nbt = itemCapability.serializeNBT();
    	tileCapability.deserializeNBT(nbt);
    	
    	tileentity.markDirty();
    	
    	return true;
    }


	/**
	 * baubles用
	 * バックパックを胴に装備できるようにする
	 * @param
	 * @return
	 */
	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}
	
}

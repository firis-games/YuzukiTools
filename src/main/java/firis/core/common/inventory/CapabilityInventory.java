package firis.core.common.inventory;

import firis.yuzukitools.common.capability.TileEntityItemStackHandler;
import firis.yuzukitools.common.helpler.VanillaNetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;


public class CapabilityInventory implements IInventory {

	/**
	 * コンストラクタ(Block)
	 */
	public CapabilityInventory(TileEntity tile) {
		this.tile = tile;
		this.itemStack = null;
	}
	
	/**
	 * コンストラクタ(ItemStack)
	 * @param tile
	 */
	public CapabilityInventory(ItemStack stack) {
		this.tile = null;
		this.itemStack = stack;
	}
	
	/**
	 * ItemHandlerをもつTileEntity
	 */
	protected TileEntity tile;
	public TileEntity getTileEntity() {
		return this.tile;
	}
	
	/**
	 * ItemHandlerをもつTileEntity
	 */
	protected ItemStack itemStack;
	
	/**
	 * ItemHandlerをもつItemStack
	 */
	
	/**
	 * 内部インベントリ
	 */
	protected TileEntityItemStackHandler getInventory() {
		TileEntityItemStackHandler capability = null;
		if (this.tile != null) {
			capability = (TileEntityItemStackHandler) this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		} else if (this.itemStack != null) {
			capability = (TileEntityItemStackHandler) this.itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		return capability;
	}
	
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return this.getInventory().getSlots();
	}

	/**
	 * 後回し
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		TileEntityItemStackHandler capability = this.getInventory();
		if (capability == null) return ItemStack.EMPTY.copy();
		
		return capability.getStackInSlot(index);
	}

	/**
	 * 指定数のItemStackをSlotから取得
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		TileEntityItemStackHandler capability = this.getInventory();
		if (capability == null) return ItemStack.EMPTY.copy();
		
		ItemStack stack = capability.directExtractItem(index, count, false);
		this.markDirty();
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		TileEntityItemStackHandler capability = this.getInventory();
		if (capability == null) return ItemStack.EMPTY.copy();
		
		//指定スロットから取得
		ItemStack stack = capability.directExtractItem(index, capability.getStackInSlot(index).getCount(), false);
		this.markDirty();
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		TileEntityItemStackHandler capability = this.getInventory();
		if (capability == null) return;
		
		capability.setStackInSlot(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		if (tile != null) VanillaNetworkHelper.sendPacketTileEntity(tile);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		IItemHandler capability = this.getInventory();
		if (capability == null) return false;
		
		//01.isItemValidチェック
		boolean ret = capability.isItemValid(index, stack);
		
		return ret;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}

}

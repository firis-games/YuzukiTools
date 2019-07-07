package firis.yuzukitools.common.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityItemStackHandler extends ItemStackHandler {

	/**
	 * アイテム搬入可能スロット
	 */
	protected List<Integer> inputSlotIndex = new ArrayList<Integer>();
	
	/**
	 * アイテム搬出可能スロット
	 */
	protected List<Integer> outputSlotIndex = new ArrayList<Integer>();

	/**
	 * コンストラクタ
	 * @param size
	 */
	public TileEntityItemStackHandler(int size) {
		super(size);
		
		//初期化
		this.inputSlotIndex = IntStream.range(0, size).boxed().collect(Collectors.toList());
		this.outputSlotIndex = IntStream.range(0, size).boxed().collect(Collectors.toList());
	}
	
	public void setInputSlot(List<Integer> list) {
		this.inputSlotIndex = list;
	}
	
	public void setOutputSlot(List<Integer> list) {
		this.outputSlotIndex = list;
	}
	
	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (this.inputSlotIndex.indexOf(slot) < 0) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	@Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.outputSlotIndex.indexOf(slot) < 0) {
			return ItemStack.EMPTY.copy();
		}
		return super.extractItem(slot, amount, simulate);
	}
	
	/**
	 * 搬入チェックを行わないinsertItem
	 * @param slot
	 * @param stack
	 * @param simulate
	 * @return
	 */
	public ItemStack directInsertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	/**
	 * 搬出チェックを行わないoutputSlotIndex
	 * @param slot
	 * @param amount
	 * @param simulate
	 * @return
	 */
	public ItemStack directExtractItem(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}
}

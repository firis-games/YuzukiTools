package firis.yuzukitools.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class AbstractContainer extends Container {

	protected final IInventory inventory;

	protected final int startIndexPlayerSlot;
	
	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public AbstractContainer(IInventory inventory, InventoryPlayer playerInv) {
		
		this.inventory = inventory;
		
		//Inventoryスロット初期化
		this.initInventorySlot(inventory);
		
		//プレイヤースロットの開始位置を設定
		this.startIndexPlayerSlot = this.inventorySlots.size();
		
		//Playerスロットの初期化
		this.initPlayerInventorySlot(playerInv);
	}
	
	/**
	 * Inventoryスロット初期化
	 * @param inventory
	 */
	protected abstract void initInventorySlot(IInventory inventory);
	
	/**
	 * プレイヤースロットの初期
	 */
	protected abstract void initPlayerInventorySlot(InventoryPlayer playerInv);
	
	/**
     * shift-click制御
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        //プレイヤーインベントリの開始index
        int playerInventoryIndex = this.startIndexPlayerSlot;
        
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (index < playerInventoryIndex) {
            	//コンテナーインベントリ -> プレイヤーインベントリ
            	if (!this.mergeItemStack(itemstack1, playerInventoryIndex, this.inventorySlots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            	
            } else {
            	//プレイヤーインベントリ -> コンテナーインベントリ
            	if (!this.mergeItemStack(itemstack1, 0, playerInventoryIndex, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	/**
	 * 標準的なPlayerInventory生成
	 */
	protected void initPlayerInventorySlotStandard(InventoryPlayer playerInv, int x, int y) {
		
		//playerインベントリ基準座標設定
		int xBasePos = x;
		int yBasePos = y;
		
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
            	int slotIndex = j + i * 9 + 9; //index 9 からスタート
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
            }
        }
        
        //playerホットバー
        yBasePos = yBasePos + 58;
		for (int i = 0; i < 9; i++) {
			int slotIndex = i; //index 0 からスタート
        	int xPos = xBasePos + i * 18;
        	int yPos = yBasePos;
        	this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
		}
		
	}

}

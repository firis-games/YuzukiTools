package firis.yuzukitools.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class YKContainerBackpack extends AbstractContainer {

	/**
	 * コンストラクタ
	 * @param inventory
	 * @param playerInv
	 */
	public YKContainerBackpack(IInventory inventory, InventoryPlayer playerInv) {
		this(inventory, playerInv, -1);
	}
	
	/**
	 * コンストラクタ ロック制御
	 * @param inventory
	 * @param playerInv
	 * @param locked
	 */
	public YKContainerBackpack(IInventory inventory, InventoryPlayer playerInv, int lockedSlot) {
		super(inventory, playerInv);
		this.initPlayerInventorySlotAfter(playerInv, lockedSlot);
	}
	
	/**
	 * Inventoryスロット初期化
	 * @param inventory
	 */
	@Override
	protected void initInventorySlot(IInventory inventory) {
		
		//基準座標
		int xBasePos = 8;
		int yBasePos = 18;

		int invX = 9;
		int invY = 6;
		int baseSlot = 0;
		
		//inventoryスロット
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new Slot(inventory, slotIndex, xPos, yPos) {
            		public boolean isItemValid(ItemStack stack)
            	    {
            			//IItemHandlerを持たないものを入れることができる
            			if (stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) != null) {
            				return false;
            			}
            	        return true;
            	    }
            	});
            }
        }
	}
	
	/**
	 * プレイヤースロットの初期(ロック処理を行うため無効化)
	 */
	@Override
	protected void initPlayerInventorySlot(InventoryPlayer playerInv) {
	}
	
	/**
	 * プレイヤースロットの初期
	 * @param playerInv
	 */
	protected void initPlayerInventorySlotAfter(InventoryPlayer playerInv, int lockedSlot) {
		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;
		
		//playerインベントリ基準座標設定
		xBasePos = 8;
		yBasePos = 140;
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
        	if (lockedSlot != slotIndex) {
        		this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
        	} else {
        		//ロックスロット
        		this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos) {
        			@Override
        			public boolean canTakeStack(EntityPlayer playerIn)
        		    {
        		        return false;
        		    }
        		});
        	}
		}
	}

}

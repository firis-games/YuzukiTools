package firis.yuzukitools.common.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class YKContainerBackpack extends AbstractContainer {

	public YKContainerBackpack(IInventory inventory, InventoryPlayer playerInv) {
		super(inventory, playerInv);
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
            	this.addSlotToContainer(new Slot(inventory, slotIndex, xPos, yPos));
            }
        }
	}
	
	/**
	 * プレイヤースロットの初期
	 */
	@Override
	protected void initPlayerInventorySlot(InventoryPlayer playerInv) {
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
        	this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
		}
	}

}

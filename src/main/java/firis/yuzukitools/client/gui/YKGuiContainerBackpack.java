package firis.yuzukitools.client.gui;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.container.YKContainerBackpack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class YKGuiContainerBackpack extends AbstractGuiContainer {

	/**
	 * コンストラクタ
	 */
	public YKGuiContainerBackpack(IInventory inventory, InventoryPlayer playerInv) {
		this(inventory, playerInv, -1);
	}
	
	/**
	 * コンストラクタ ロック制御
	 */
	public YKGuiContainerBackpack(IInventory inventory, InventoryPlayer playerInv, int lockedSlot) {
		
		super(new YKContainerBackpack(inventory, playerInv, lockedSlot));

		this.inventory = inventory;
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation(YuzukiTools.MODID, 
				"textures/gui/backpack.png");
		
		//GUIタイトル
		this.guiTitle = "gui.backpack.name";
		
		this.guiWidth = 175;
		this.guiHeight = 221;
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
	}
	
}

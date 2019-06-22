package firis.yuzukitools.client.gui;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.container.YKContainerBackpack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class YKGuiContainerBackpack extends AbstractGuiContainer {

	public YKGuiContainerBackpack(IInventory inventory, InventoryPlayer playerInv) {
		
		super(new YKContainerBackpack(inventory, playerInv));
		
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

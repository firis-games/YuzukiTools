package firis.yuzukitools.client.gui;

import java.text.NumberFormat;

import firis.core.common.inventory.CapabilityInventory;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.container.YKContainerElectricFurnace;
import firis.yuzukitools.common.tileentity.YKTileElectricFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;

public class YKGuiElectricFurnace extends AbstractGuiContainer {

	/**
	 * エネルギー管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * 燃焼時間描画用
	 */
	public YKTileElectricFurnace tile;
	
	/**
	 * コンストラクタ
	 * @param inventory
	 * @param playerInv
	 */
	public YKGuiElectricFurnace(IInventory inventory, InventoryPlayer playerInv) {
		super(new YKContainerElectricFurnace(inventory, playerInv));
		
		this.inventory = inventory;
		
		this.tile = (YKTileElectricFurnace) ((CapabilityInventory) inventory).getTileEntity();
		this.energy = (TileEntityEnergyStorage) ((CapabilityInventory) inventory).getTileEntity().getCapability(CapabilityEnergy.ENERGY, null);
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation(YuzukiTools.MODID, 
				"textures/gui/electric_furnace.png");
		
		//GUIタイトル
		this.guiTitle = "gui.electric_furnace.name";
		this.guiTitleCenter = true;
		
		this.guiWidth = 176;
		this.guiHeight = 166;
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		
		//エネルギー描画用
		ResourceLocation rl = new ResourceLocation(YuzukiTools.MODID, "blocks/energy");
		this.gageTxtures = Minecraft.getMinecraft().getTextureMapBlocks()
				.getTextureExtry(rl.toString());
	}
	
	protected TextureAtlasSprite gageTxtures = null;
	
	/**
	 * ゲージの描画
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		
		//ゲージの描画
		double gage = (double)this.energy.getEnergyStored() / (double)this.energy.getMaxEnergyStored();
		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GuiDrawHelper.drawGage(this, gageTxtures, x + 27, y + 17, 8, 52, gage);
		
		this.mc.getTextureManager().bindTexture(guiTextures);
        
		//稼動状態
		if (this.tile.isActive()) {
        	//ソーラーが動いていない場合
        	this.drawTexturedModalRect(x + 53, y + 57, 176, 0, 14, 14);
        }
		
		//矢印の描画
		GuiDrawHelper.drawArrow(this, x + 79, y + 34, 
				176, 14, 24, 17, this.tile.getProgress());
	}
	
	/**
	 * ToolTip表示
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		//描画位置を計算
        int tip_x = (this.width - xSize) / 2;
        int tip_y = (this.height - ySize) / 2;

        tip_x += 27;
        tip_y += 17;
        
		RenderHelper.disableStandardItemLighting();
		
		//ツールチップ
		if (tip_x <= mouseX && mouseX <= tip_x + 8
        		&& tip_y <= mouseY && mouseY <= tip_y + 52) {
			
        	Integer mana = energy.getEnergyStored();
            
        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);

    		String unit = I18n.format("info.energy_unit.name");
    		
        	this.drawHoveringText(TextFormatting.LIGHT_PURPLE + NumberFormat.getNumberInstance().format(mana) + unit, xAxis, yAxis);
        }
		
		RenderHelper.enableGUIStandardItemLighting();
	}
}

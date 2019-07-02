package firis.yuzukitools.client.gui;

import java.text.NumberFormat;

import firis.core.common.inventory.CapabilityInventory;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.container.YKContainerSolarCharger;
import firis.yuzukitools.common.tileentity.YKTileSolarCharger;
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

public class YKGuiSolarCharger extends AbstractGuiContainer {

	/**
	 * エネルギー管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * ソーラー稼動描画用
	 */
	public YKTileSolarCharger tile;
	
	/**
	 * コンストラクタ
	 * @param inventory
	 * @param playerInv
	 */
	public YKGuiSolarCharger(IInventory inventory, InventoryPlayer playerInv) {
		super(new YKContainerSolarCharger(inventory, playerInv));
		
		this.inventory = inventory;
		
		this.tile = (YKTileSolarCharger) ((CapabilityInventory) inventory).getTileEntity();
		this.energy = (TileEntityEnergyStorage) ((CapabilityInventory) inventory).getTileEntity().getCapability(CapabilityEnergy.ENERGY, null);
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation(YuzukiTools.MODID, 
				"textures/gui/solar_charger.png");
		
		//GUIタイトル
		this.guiTitle = "gui.solar_charger.name";
		this.guiTitleCenter = true;
		
		this.guiWidth = 176;
		this.guiHeight = 166;
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		
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
		GuiDrawHelper.drawGage(this, gageTxtures, x + 72, y + 26, 32, 50, gage);
		
		//メモリの描画
		this.mc.getTextureManager().bindTexture(guiTextures);
        this.drawTexturedModalRect(x + 72, y + 25, 176, 0, 10, 50);
        
        //ソーラーの判定
        if (!this.tile.isSolarChargerActive()) {
        	//ソーラーが動いていない場合
        	this.drawTexturedModalRect(x + 30, y + 26, 176, 72, 16, 16);
        } else if (this.tile.isSolarRedstonePower()) {
        	//レッドストーンモードON
        	this.drawTexturedModalRect(x + 30, y + 26, 176, 56, 16, 16);
        }
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

        tip_x += 72;
        tip_y += 26;
        
		RenderHelper.disableStandardItemLighting();
		
		//ツールチップ
		if (tip_x <= mouseX && mouseX <= tip_x + 32
        		&& tip_y <= mouseY && mouseY <= tip_y + 50) {
			
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

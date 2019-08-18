package firis.yuzukitools.client.gui;

import java.text.NumberFormat;

import firis.core.common.inventory.CapabilityInventory;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.capability.TileEntityEnergyStorage;
import firis.yuzukitools.common.capability.TileEntityFluidHandler;
import firis.yuzukitools.common.container.YKContainerKitchenGarden;
import firis.yuzukitools.common.tileentity.YKTileKitchenGarden;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class YKGuiKitchenGarden extends AbstractGuiContainer {

	/**
	 * エネルギー管理用
	 */
	public TileEntityEnergyStorage energy;
	
	/**
	 * 液体管理用
	 */
	public TileEntityFluidHandler fluid;
	
	
	/**
	 * 燃焼時間描画用
	 */
	public YKTileKitchenGarden tile;
	
	/**
	 * コンストラクタ
	 * @param inventory
	 * @param playerInv
	 */
	public YKGuiKitchenGarden(IInventory inventory, InventoryPlayer playerInv) {
		super(new YKContainerKitchenGarden(inventory, playerInv));
		
		this.inventory = inventory;
		
		//描画に必要な情報を取得
		this.tile = (YKTileKitchenGarden) ((CapabilityInventory) inventory).getTileEntity();
		this.energy = (TileEntityEnergyStorage) ((CapabilityInventory) inventory).getTileEntity().getCapability(CapabilityEnergy.ENERGY, null);
		this.fluid = (TileEntityFluidHandler) ((CapabilityInventory) inventory).getTileEntity().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation(YuzukiTools.MODID, 
				"textures/gui/kitchen_garden.png");
		
		//GUIタイトル
		this.guiTitle = "gui.kitchen_garden.name";
		this.guiTitleCenter = true;
		
		this.guiWidth = 176;
		this.guiHeight = 166;
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		
		//エネルギー描画用
		ResourceLocation rl = new ResourceLocation(YuzukiTools.MODID, "blocks/energy");
		this.gageTxtures = Minecraft.getMinecraft().getTextureMapBlocks()
				.getTextureExtry(rl.toString());
		
		//水描画用
		Fluid fluid = FluidRegistry.getFluid("water");
		ResourceLocation fluidLocation = fluid.getStill();
		this.gageWaterTxtures = Minecraft.getMinecraft().getTextureMapBlocks()
				.getTextureExtry(fluidLocation.toString());
	}
	
	protected TextureAtlasSprite gageTxtures = null;
	
	protected TextureAtlasSprite gageWaterTxtures = null;
	
	/**
	 * ゲージの描画
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		
		//電気ゲージの描画
		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		double gage = (double)this.energy.getEnergyStored() / (double)this.energy.getMaxEnergyStored();
		GuiDrawHelper.drawGage(this, gageTxtures, x + 27, y + 17, 8, 52, gage);
		
		//水ゲージの描画
		double waterGage = (double)this.fluid.getLiquid() / (double)this.fluid.getMaxLiquid();
		GuiDrawHelper.drawGage(this, gageWaterTxtures, x + 38, y + 17, 8, 52, waterGage);
		
		this.mc.getTextureManager().bindTexture(guiTextures);
		
		//矢印の描画
		GuiDrawHelper.drawArrow(this, x + 81, y + 21, 
				176, 0, 24, 17, this.tile.getGageProgress());
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

        //エネルギーゲージ
        int tip_x_1 = tip_x + 27;
        int tip_y_1 = tip_y + 17;
        
        //水ゲージ
        int tip_x_2 = tip_x + 38;
        int tip_y_2 = tip_y + 17;
        
		RenderHelper.disableStandardItemLighting();
		
		//ツールチップ
		if (tip_x_1 <= mouseX && mouseX <= tip_x_1 + 8
        		&& tip_y_1 <= mouseY && mouseY <= tip_y_1 + 52) {
			
        	Integer mana = energy.getEnergyStored();
            
        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);

    		String unit = I18n.format("info.energy_unit.name");
    		
        	this.drawHoveringText(TextFormatting.LIGHT_PURPLE + NumberFormat.getNumberInstance().format(mana) + unit, xAxis, yAxis);
        } else if (tip_x_2 <= mouseX && mouseX <= tip_x_2 + 8
        		&& tip_y_2 <= mouseY && mouseY <= tip_y_2 + 52) {
			
        	Integer water = fluid.getLiquid();
            
        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);

    		String unit = "mb";
    		
        	this.drawHoveringText(TextFormatting.AQUA + NumberFormat.getNumberInstance().format(water) + unit, xAxis, yAxis);
        }
		
		RenderHelper.enableGUIStandardItemLighting();
	}
}

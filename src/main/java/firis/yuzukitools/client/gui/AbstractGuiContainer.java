package firis.yuzukitools.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractGuiContainer extends GuiContainer {

	/**
	 * GUIテクスチャ
	 */
	protected ResourceLocation guiTextures = null;

	/**
	 * GUIタイトル
	 */
	protected String guiTitle = "";
	
	protected boolean guiTitleCenter = false;

	/**
	 * GUIサイズ
	 */
	protected int guiWidth = 176;
	protected int guiHeight = 166;

	protected IInventory inventory;

	public AbstractGuiContainer(Container container) {
		
		super(container);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		// テクスチャロード
		this.mc.getTextureManager().bindTexture(guiTextures);

		// 画面へバインド（かまどのGUIサイズ）
		int xSize = this.guiWidth;
		int ySize = this.guiHeight;

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;

		// 画面へ描画
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		RenderHelper.disableStandardItemLighting();
		
		//xx_xx.langから取得
		TextComponentTranslation langtext = 
				new TextComponentTranslation(this.guiTitle, new Object[0]);
        String text = langtext.getFormattedText();

        
        if (guiTitleCenter) {
        	int x = this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
            int y = 6;
            
            //タイトル文字
            this.fontRenderer.drawString(text, x, y, 4210752);
        }else {
	        //左寄せ
	        int x = 8;
	        int y = 6;
	        
	        //タイトル文字
	        this.fontRenderer.drawString(text, x, y, 4210752);
        }
        
        RenderHelper.enableGUIStandardItemLighting();
    }
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * 背景色黒とアイテムのツールチップを表示するために必要
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
}

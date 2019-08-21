package firis.yuzukitools.client.tesr;

import firis.yuzukitools.common.tileentity.YKTileKitchenGarden;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class YKTileKitchenGardenSpRenderer extends TileEntitySpecialRenderer<YKTileKitchenGarden> {

	public YKTileKitchenGardenSpRenderer(){
		
	}
	
	/**
	 * Renderer
	 */
	@Override
	public void render(YKTileKitchenGarden te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 1, (float)z + 1D);
        
        this.doRender(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.popMatrix();
	}
	
	public void doRender(YKTileKitchenGarden te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		//ライト設定（これをしないと描画したものが暗くなる）
		RenderHelper.disableStandardItemLighting();
		
		//色を指定しないと描画したブロックが暗くなる
		GlStateManager.color(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		//種の描画
		IBlockState seedState = te.getRenderStateSeed();
		if (seedState != null) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.8F, 0.8F, 0.8F);
			GlStateManager.translate(0.1F, 0, -0.1F);
			
			//描画
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(
					seedState, 1.0F);
			
			GlStateManager.popMatrix();
			
			//BlockDoublePlantの例外処理
			if (seedState.getBlock() instanceof BlockDoublePlant) {
				//BlockDoublePlant blockDoublePlant = (BlockDoublePlant) seedState.getBlock();
				seedState = seedState.withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER);
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 0.79F, 0.0F);
				
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(
						seedState, 1.0F);
				GlStateManager.popMatrix();
				
			}
		}
		
		//土壌の描画
		IBlockState soilState = te.getRenderStateSoil();
		if (soilState != null) {
			GlStateManager.pushMatrix();
			
			GlStateManager.scale(0.8F, 0.8F, 0.8F);
			GlStateManager.translate(0.1F, 0, -0.1F);
			GlStateManager.translate(0.0F, -0.999F, 0.0F);
			
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(
					soilState, 1.0F);
			
			GlStateManager.popMatrix();
		}
		
		RenderHelper.enableStandardItemLighting();
	}
	
}

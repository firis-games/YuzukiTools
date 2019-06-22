package firis.yuzukitools.client.layer;

import firis.yuzukitools.YuzukiTools.YKBlocks;
import firis.yuzukitools.common.item.YKItemBlockBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * BackPack描画用
 * @author computer
 *
 */
public final class YKBackPackLayer implements LayerRenderer<EntityPlayer> {

	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		if (!this.isRender(player)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		
		//ブロックを描画
		//========================================
		//スニーク位置調整
		rotateSneaking(player);
		
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(180, 1, 0, 0);
		
		
		GlStateManager.translate(-1F, -0.75F, 0.5F);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(YKBlocks.BACKPACK.getDefaultState(), 1.0F);
		//========================================
		
		GlStateManager.popMatrix();
	}
	
	/**
	 * 描画するかの判断を行う
	 * @param player
	 * @return
	 */
	private boolean isRender(EntityPlayer player) {
		
		boolean ret = false;
		
		//チェスト枠をチェックする
		ItemStack stack = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
		if (stack.getItem() instanceof YKItemBlockBackpack) {
			ret = true;
		}
		
		return ret;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	/**
	 * スニーク時の角度調整
	 * @param player
	 */
	private void rotateSneaking(EntityPlayer player) {
		
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}
	}

}

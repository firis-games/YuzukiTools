package firis.yuzukitools.client.tesr;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;

import firis.core.client.ShaderHelper;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.tileentity.YKTileInstantHouse;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class YKTileInstantHouseSpRenderer extends TileEntitySpecialRenderer<YKTileInstantHouse> {

	protected Template template = null;
	protected PlacementSettings placementsettings = null;
	
	public YKTileInstantHouseSpRenderer(){
	}
	
	/**
	 * Renderer
	 */
	@Override
	public void render(YKTileInstantHouse te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        
        this.doRender(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.popMatrix();
        
		
	}
	
	/**
	 * インスタントハウス描画
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 * @param partialTicks
	 * @param destroyStage
	 * @param alpha
	 */
	public void doRender(YKTileInstantHouse te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		
		//インスタントハウスオブジェクト生成
		if (!"".equals(te.getTemplate()) && this.template == null) {
			//Template初期化
			ResourceLocation rl = new ResourceLocation(YuzukiTools.MODID, te.getTemplate());
			this.template = YKTileInstantHouseSpRenderer.getTemplateToJar(rl);

			//設定初期化
			this.placementsettings =  new PlacementSettings();
		}
		
		//テンプレートガない場合は何もしない
		if (this.template == null) {
			return;
		}
		
		
		EnumFacing facing = te.getFacing();
		
		BlockPos facingPos = new BlockPos(0, 0, 0);
		
		int facing_x = 7;
        int facing_z = 3;
        
        //位置調整
        //北（標準）
        switch (facing) {
        case NORTH:
        	this.placementsettings.setRotation(Rotation.NONE);
        	facingPos = facingPos.north(facing_x).west(facing_z);
        	break;
        case SOUTH:
        	this.placementsettings.setRotation(Rotation.CLOCKWISE_180);
        	facingPos = facingPos.south(facing_x).east(facing_z);
        	break;
        case EAST:
        	this.placementsettings.setRotation(Rotation.CLOCKWISE_90);
        	facingPos = facingPos.north(facing_z).east(facing_x);
        	break;
        case WEST:
        	this.placementsettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
        	facingPos = facingPos.south(facing_z).west(facing_x);
        	break;
       	default:
        }
		
		//位置を調整
		//GlStateManager.translate(-4, 0, -9);
		GlStateManager.translate(facingPos.getX(), facingPos.getY(), facingPos.getZ());
		
		
		//透過シェーダー
		ShaderHelper.useShader(YuzukiTools.shader_alpha);

		
		for (Template.BlockInfo blockInfo : this.template.blocks) {
			
			//位置を調整
			BlockPos pos = Template.transformedBlockPos(this.placementsettings, blockInfo.pos);

			int posX = pos.getX();
			int posY = pos.getY();
			int posZ = pos.getZ();

			IBlockState state = blockInfo.blockState;
			
			if (state.getBlock() instanceof BlockDoor) {
			
				//直接方向を書き換える
				EnumFacing rotationFacing = Rotation.CLOCKWISE_90.rotate(facing);
				state = state.withProperty(BlockDoor.FACING, rotationFacing);

			} else if (state.getBlock() instanceof BlockChest
					|| state.getBlock() == Blocks.STAINED_GLASS_PANE
					) {
				//方向設定がうまくいかないのでチェストと板ガラスは描画しない
				continue;
			} else {
				//デフォルトの位置がおかしいのでずらす
				state = state.withRotation(Rotation.CLOCKWISE_90);
				
				//方角にあわせて回転させる
				state = state.withRotation(this.placementsettings.getRotation());
			}
			
			GlStateManager.pushMatrix();

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			//座標設定
			GlStateManager.translate(posX, posY, posZ + 1);
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.disableDepth();
			
			//ブロック描画
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			//描画
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(
					state, 1.0F);
			
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
			
		}
		
		ShaderHelper.releaseShader();
		
	}
	
	/**
	 * Jarファイル内のテンプレートを取得する
	 * @return
	 */
	public static Template getTemplateToJar(ResourceLocation rl) {
		
		//ファイル読み込み
		InputStream inputstream = FMLCommonHandler.instance()
				.getClass().getClassLoader()
				.getResourceAsStream("assets/" + rl.getResourceDomain() + "/structures/" + rl.getResourcePath() + ".nbt");
		
		NBTTagCompound nbttagcompound = null;
		
		try {
			nbttagcompound = CompressedStreamTools.readCompressed(inputstream);
		} catch (IOException e) {
		}
		
		//Template生成
		Template template = new Template();
		template.read(nbttagcompound);
		
		return template;
	}
	
}

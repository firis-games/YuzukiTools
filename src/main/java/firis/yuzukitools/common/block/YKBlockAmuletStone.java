package firis.yuzukitools.common.block;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.tileentity.YKTileAmuletStone;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 魔除けの石
 * @author firis-games
 *
 */
public class YKBlockAmuletStone extends AbstractBlockContainer {

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public YKBlockAmuletStone() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setLightLevel(0.5F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}

	/**
	 * TileEntity
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileAmuletStone();
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.amulet_stone.info"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("tile.amulet_stone.details"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + "Range : 5×5 Chunk");
    }
	
}

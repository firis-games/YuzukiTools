package firis.yuzukitools.mobbottle.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.mobbottle.common.item.FItemMobBottle;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * モブボトルプラス
 * @author firis-games
 *
 */
public class FItemMobBottlePlus extends FItemMobBottle {

	public FItemMobBottlePlus(Block block) {
		super(block);
	}
	
	@Override
	protected Item getItemMobBottle() {
		return YKItems.MOB_BOTTLE_PLUS;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.mob_bottle_plus.info"));
		if (stack.hasTagCompound()) {
			//Mob名
			if (stack.getTagCompound().hasKey("mob_name")) {
				tooltip.add("Mob : " + stack.getTagCompound().getString("mob_name"));
			}
		}
    }
}

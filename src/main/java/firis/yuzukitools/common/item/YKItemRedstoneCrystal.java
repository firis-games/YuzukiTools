package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemRedstoneCrystal extends AbstractEnergyItem {

	/**
	 * コンストラクタ
	 */
	public YKItemRedstoneCrystal() {
		super(1000000);
	}

	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.redstone_crystal.info"));
    	super.addInformation(stack, player, tooltip, advanced);
    }
}

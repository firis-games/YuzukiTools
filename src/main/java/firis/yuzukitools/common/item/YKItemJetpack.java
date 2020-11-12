package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(modid="baubles", iface="baubles.api.IBauble")
public class YKItemJetpack extends AbstractEnergyItemArmor implements IItemJetpack, IBauble {
	
	public YKItemJetpack() {
		super(ArmorMaterial.LEATHER, EntityEquipmentSlot.CHEST, 200000);
	}
	
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return YuzukiTools.MODID + ":textures/models/armor/jetpack_1.png";
    }

	/**
	 * baubles用
	 * ジェットパックを胴に装備できるようにする
	 * @param
	 * @return
	 */
	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.jetpack.info"));
		tooltip.add(TextFormatting.DARK_AQUA + I18n.format("item.jetpack.details"));
    	super.addInformation(stack, player, tooltip, advanced);
    }
	
}

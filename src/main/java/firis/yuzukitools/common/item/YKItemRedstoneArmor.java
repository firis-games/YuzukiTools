package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemRedstoneArmor extends AbstractEnergyItemArmor {
    
    protected final EntityEquipmentSlot armorType;
    
	/**
	 * コンストラクタ
	 */
	public YKItemRedstoneArmor(EntityEquipmentSlot armorType) {
		
		super(ArmorMaterial.IRON, armorType, AbstractEnergyItemTool.DEFAULT_TOOL_CAPACITY * 2);
		
		this.armorType = armorType;
	}
	    
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    	if (slot != EntityEquipmentSlot.LEGS) {
    		return YuzukiTools.MODID + ":textures/models/armor/redstone_layer_1.png";
    	} else {
    		return YuzukiTools.MODID + ":textures/models/armor/redstone_layer_2.png";    		
    	}
    }
    
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.redstone_armor.info"));
		tooltip.add(TextFormatting.DARK_AQUA + I18n.format("item.redstone_armor.details"));
    	super.addInformation(stack, player, tooltip, advanced);
    }

}

package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

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

}

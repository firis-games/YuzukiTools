package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Jetpackレッドストーンアーマー
 * @author computer
 *
 */
public class YKItemRedstoneArmorWithJetpack extends YKItemRedstoneArmor {

	public YKItemRedstoneArmorWithJetpack(EntityEquipmentSlot armorType) {
		super(armorType);
		this.capacity = 500000;
	}
	
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    	if (slot != EntityEquipmentSlot.LEGS) {
    		return YuzukiTools.MODID + ":textures/models/armor/redstone_jp_layer_1.png";
    	} else {
    		return YuzukiTools.MODID + ":textures/models/armor/redstone_layer_2.png";    		
    	}
    }

}

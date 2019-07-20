package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class YKItemJetpack extends AbstractEnergyItemArmor {

	public YKItemJetpack() {
		super(ArmorMaterial.IRON, EntityEquipmentSlot.CHEST, 100000);
	}
	
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return YuzukiTools.MODID + ":textures/models/armor/jetpack_1.png";
    }
    
    /**
     * ジェットパックを装備しているかつエネルギーを判断する
     * @param player
     * @return
     */
    public static boolean isActiveJetpack(EntityPlayer player) {
    
    	ItemStack chestplate = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
		
    	//Jetpackでない
    	if (chestplate.isEmpty() || chestplate.getItem() != YKItems.JETPACK) {
    		return false;
    	}
    	
    	IEnergyStorage storage = chestplate.getCapability(CapabilityEnergy.ENERGY, null);
    	
    	//消耗値と同じとする
    	if (storage.getEnergyStored() < AbstractEnergyItemTool.USE_ENERGY) {
    		return false;
    	}
    	
    	return true;
    }

}
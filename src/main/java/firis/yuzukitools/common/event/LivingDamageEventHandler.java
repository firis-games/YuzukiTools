package firis.yuzukitools.common.event;

import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class LivingDamageEventHandler {

	/**
	 * 
	 * @param envet
	 */
	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
		
		//落下ダメージの無効化
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		
		ItemStack helmet = player.inventory.armorInventory.get(EntityEquipmentSlot.HEAD.getIndex());
		ItemStack chestplate = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
		ItemStack leggings = player.inventory.armorInventory.get(EntityEquipmentSlot.LEGS.getIndex());
		ItemStack boots = player.inventory.armorInventory.get(EntityEquipmentSlot.FEET.getIndex());
		
		
		//レッドストーン防具かのチェック
		if (helmet.isEmpty() || helmet.getItem() != YKItems.REDSTONE_HELMET ) return;
		if (chestplate.isEmpty() || chestplate.getItem() != YKItems.REDSTONE_CHESTPLATE ) return;
		if (leggings.isEmpty() || leggings.getItem() != YKItems.REDSTONE_LEGGINGS ) return;
		if (boots.isEmpty() || boots.getItem() != YKItems.REDSTONE_BOOTS ) return;
		
		
		//落下ダメージ軽減
		if (event.getSource() == DamageSource.FALL) {
			
			int minEnergy;
			minEnergy = helmet.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
			minEnergy = Math.min(minEnergy, chestplate.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
			minEnergy = Math.min(minEnergy, leggings.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
			minEnergy = Math.min(minEnergy, boots.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
			
			//エネルギー10につき1ダメージ無効化
			int resist = (int) Math.min(event.getAmount(), minEnergy / 10);
			
			//ダメージを軽減してエネルギーを消費
			event.setAmount(event.getAmount() - resist);
			int resistEnergy = resist * 10;
			helmet.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
			chestplate.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
			leggings.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
			boots.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
			
			return;
		}
		
		
		
		
	}
	
}

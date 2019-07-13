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
		
		//軽減効果発動条件チェック
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
		
		//ダメージ係数を判断
		int damageRate = 0;
		int damageLimit = 0;
		
		String damageType = event.getSource().getDamageType();

		if (DamageSource.FALL.getDamageType().equals(damageType)
				|| DamageSource.ON_FIRE.getDamageType().equals(damageType)
				|| DamageSource.IN_FIRE.getDamageType().equals(damageType)) {
			//落下ダメージ or 炎上ダメージ
			damageRate = 1;
			damageLimit = 0;
		} else if (DamageSource.LAVA.getDamageType().equals(damageType)) {
			//溶岩ダメージ
			damageRate = 5;
			damageLimit = 0;
		} else if ("arrow".equals(damageType)) {
			//矢ダメージ
			damageRate = 10;
			damageLimit = 1;
		} else if ("explosion.player".equals(damageType)) {
			//爆発ダメージ
			damageRate = 5;
			damageLimit = 4;
		} else if ("mob".equals(damageType)) {
			//mobダメージ
			damageRate = 10;
			damageLimit = 4;
		}
		
		//レートが0の場合は対象外
		if (damageRate == 0) return;
		
		//最大軽減ダメージ
		int regDamage = Math.max((int) Math.ceil(event.getAmount()) - damageLimit, 0);
		//アーマー消費ベース
		int energyUnit = 10 * damageRate;
		
		//最大軽減ダメージが0の場合は何もしない
		if (regDamage == 0) return;
		
		int minEnergy;
		minEnergy = helmet.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
		minEnergy = Math.min(minEnergy, chestplate.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
		minEnergy = Math.min(minEnergy, leggings.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
		minEnergy = Math.min(minEnergy, boots.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored());
		
		//エネルギー10につき1ダメージ無効化
		int resist = (int) Math.min(regDamage, minEnergy / energyUnit);
		
		//ダメージを軽減してエネルギーを消費
		event.setAmount(event.getAmount() - resist);
		int resistEnergy = resist * energyUnit;
		helmet.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
		chestplate.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
		leggings.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
		boots.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(resistEnergy, false);
		
	}
	
}

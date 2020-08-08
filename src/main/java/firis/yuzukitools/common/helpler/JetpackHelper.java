package firis.yuzukitools.common.helpler;

import firis.yuzukitools.common.api.baubles.BaublesHelper;
import firis.yuzukitools.common.item.IItemJetpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Jetpack用のHelperクラス
 * @author firis-games
 *
 */
public class JetpackHelper {

	/**
	 * ツール1回あたりの消費量
	 */
	private static int USE_ENERGY = 50;
	
	private static int USE_BOOST_ENERGY = 500;
	
    /**
     * ジェットパックを装備しているかつエネルギーを判断する
     * @param player
     * @return
     */
    public static boolean isActiveJetpack(EntityPlayer player) {
    
    	//Baublesを想定する
    	ItemStack jetpack = getJetpackFromPlayer(player);
    	if (jetpack.isEmpty()) return false;
    	
    	IEnergyStorage storage = jetpack.getCapability(CapabilityEnergy.ENERGY, null);
    	
    	//消耗値と同じとする
    	if (storage == null || storage.getEnergyStored() < JetpackHelper.USE_ENERGY) {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * ジェットパックを装備しているかつエネルギーを判断する
     * ブーストモード用
     * @param player
     * @return
     */
    public static boolean isActiveJetpackBoost(EntityPlayer player) {
    
    	ItemStack jetpack = getJetpackFromPlayer(player);
    	if (jetpack.isEmpty()) return false;
    	
    	IEnergyStorage storage = jetpack.getCapability(CapabilityEnergy.ENERGY, null);
    	
    	//消耗値と同じとする
    	if (storage == null || storage.getEnergyStored() < JetpackHelper.USE_BOOST_ENERGY) {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * ジェットパックのエネルギー消費を行う
     */
    public static void useJetpack(EntityPlayer player) {
		//エネルギー消費
    	ItemStack jetpack = getJetpackFromPlayer(player);
		IEnergyStorage storage = jetpack.getCapability(CapabilityEnergy.ENERGY, null);
		if (storage != null) {
			storage.extractEnergy(JetpackHelper.USE_ENERGY, false);
		}
    }
    
    /**
     * ジェットパックのブーストエネルギー消費を行う
     */
    public static void useJetpackBoost(EntityPlayer player) {
		//エネルギー消費
    	ItemStack jetpack = getJetpackFromPlayer(player);
		IEnergyStorage storage = jetpack.getCapability(CapabilityEnergy.ENERGY, null);
		if (storage != null) {
			storage.extractEnergy(JetpackHelper.USE_BOOST_ENERGY, false);
		}
    }
    
    /**
     * Playerが装備しているJetpackを取得する
     * 
     * Jetpackを装備していない場合空のItemStackを返却する
     * @return
     */
    private static ItemStack getJetpackFromPlayer(EntityPlayer player) {
    	
    	ItemStack jetpack;
    	//Baubles有効の場合はBaublesスロットも確認する
		if (BaublesHelper.isLoaded()) {
			jetpack = BaublesHelper.getSlotFromBody(player);
			if (!jetpack.isEmpty() && jetpack.getItem() instanceof IItemJetpack) {
	    		return jetpack;
	    	}
		}
		
    	//チェスト防具スロット取得
    	jetpack = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
    	//jetpack扱いか判断
    	if (!jetpack.isEmpty() && jetpack.getItem() instanceof IItemJetpack) {
    		return jetpack;
    	}
    	
    	return ItemStack.EMPTY;
    }
    
}

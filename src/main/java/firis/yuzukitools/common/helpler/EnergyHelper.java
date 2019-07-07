package firis.yuzukitools.common.helpler;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHelper {

	
	/**
	 * IEnergyStorage同士のエネルギー移動
	 * @param fromEnergy
	 * @param toEnergy
	 * @param energy
	 */
	public static void moveEnergy(IEnergyStorage fromEnergy, IEnergyStorage toEnergy, int energy) {
		
		//満充電または充電出来ない場合は何もしない
		if(toEnergy.getMaxEnergyStored() <= toEnergy.getEnergyStored()
				|| !toEnergy.canReceive()) return;
		
		//最大チャージ数
		int charge = toEnergy.getMaxEnergyStored() - toEnergy.getEnergyStored();
		charge = Math.min(charge, energy);
		charge = Math.min(charge, fromEnergy.getEnergyStored());
		
		//シミュレート
		charge = toEnergy.receiveEnergy(charge, true);
		
		//実行
		toEnergy.receiveEnergy(charge, false);
		fromEnergy.extractEnergy(charge, false);
				
	}
	
}

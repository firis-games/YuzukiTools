package firis.yuzukitools.common.api.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

/**
 * Baubles連携用の処理を追加する
 * @author firis-games
 *
 */
public class BaublesHelper {
	
	private static boolean isLoaded = Loader.isModLoaded("baubles");

	/**
	 * Baublesが有効化どうかの判定
	 * @return
	 */
	public static boolean isLoaded() {
		return isLoaded;
	}
	
	/**
	 * Baubles連携用
	 * Body部に相当するスロットのアイテムを取得する
	 * 導入時：Baubles.Bodyスロット
	 * 未導入：Armor.Chestスロット
	 * @param player
	 * @return
	 */
	public static ItemStack getSlotFromArmorOrBaubles(EntityPlayer player) {
		
		ItemStack stack = ItemStack.EMPTY;
		
		if (BaublesHelper.isLoaded()) {
			//Baubles連携の場合
			stack = BaublesHelper.getSlotFromBody(player);
		} else {
			//通常
			stack = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
		}
		
		return stack;
	}
	
	/**
	 * Bodyスロットのアイテムを取得する
	 * @param player
	 * @return
	 */
	private static ItemStack getSlotFromBody(EntityPlayer player) {
		return getSlot(player, BaubleType.BODY);
	}
	
	/**
	 * Baublesスロットからアイテムを取得する
	 * @param player
	 * @return
	 */
	private static ItemStack getSlot(EntityPlayer player, BaubleType baubleType) {
		
		ItemStack baubleStack = ItemStack.EMPTY;
		
		//アイテム取得
		IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < baubleType.getValidSlots().length; i++) {
			
			int slot = baubleType.getValidSlots()[i];
			
			ItemStack work = baublesHandler.getStackInSlot(slot);
			if (!work.isEmpty()) {
				baubleStack = work;
				break;
			}
		}
		
		return baubleStack;
	}
}

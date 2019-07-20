package firis.yuzukitools.common.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import firis.yuzukitools.common.item.AbstractEnergyItemTool;
import firis.yuzukitools.common.item.YKItemJetpack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class JetpackPlayerTickEventHandler {

	@SubscribeEvent
	public static void onPlayerTickEvent(PlayerTickEvent event) {
		
		//Server側のみ処理を行う
		if (event.phase == Phase.END && event.side == Side.SERVER) {
			onJetpackPlayerTickEvent(event);
		}
		
	}
	
	//Jetpackキー入力保存用
	protected static Map<UUID, Boolean> jumpKeyMap = new HashMap<UUID, Boolean>();
	
	public static void setJetpackJumpKey(EntityPlayer player, boolean keyDown) {
		jumpKeyMap.put(player.getUniqueID(), keyDown);
	}
	public static boolean getJetpackJumpKey(EntityPlayer player) {
		boolean key = false;
		if (jumpKeyMap.containsKey(player.getUniqueID())) {
			key = jumpKeyMap.get(player.getUniqueID());
		}
		return key;
	}
	
	/**
	 * ClientTickEvent
	 * でキー入力等を制御してるよう
	 * @param event
	 */
	protected static void onJetpackPlayerTickEvent(PlayerTickEvent event) {
		
		EntityPlayer player = event.player;
		
		if (!YKItemJetpack.isActiveJetpack(player) || player.onGround) {
			setJetpackJumpKey(player, false);
			return;
		}
		
		boolean keyJump = getJetpackJumpKey(player);
		boolean isGuiScreen = player.openContainer != player.inventoryContainer;
		//Jumpボタン押下かつGUIを開いてない場合処理を行う
		if (keyJump && !isGuiScreen) {
			player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
			player.fallDistance = 0.0F;
			if (player instanceof EntityPlayerMP) {
				((EntityPlayerMP) player).connection.floatingTickCount = 0;
			}
			
			//エネルギー消費
			ItemStack chestplate = player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex());
			IEnergyStorage storage = chestplate.getCapability(CapabilityEnergy.ENERGY, null);
			storage.extractEnergy(AbstractEnergyItemTool.USE_ENERGY, false);
		}
	}
	
}

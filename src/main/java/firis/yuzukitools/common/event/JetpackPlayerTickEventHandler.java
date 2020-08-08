package firis.yuzukitools.common.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import firis.yuzukitools.common.helpler.JetpackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
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
	
	//Jetpackブーストキー入力保存用
	protected static Map<UUID, Boolean> boostKeyMap = new HashMap<UUID, Boolean>();
	
	public static void setJetpackBoostKey(EntityPlayer player, boolean keyDown) {
		boostKeyMap.put(player.getUniqueID(), keyDown);
	}
	public static boolean getJetpackBoostKey(EntityPlayer player) {
		boolean key = false;
		if (boostKeyMap.containsKey(player.getUniqueID())) {
			key = boostKeyMap.get(player.getUniqueID());
		}
		return key;
	}
	
	public static double JETPACK_BOOST = 1.5D;
	
	/**
	 * ClientTickEvent
	 * でキー入力等を制御してるよう
	 * @param event
	 */
	protected static void onJetpackPlayerTickEvent(PlayerTickEvent event) {
		
		EntityPlayer player = event.player;
		boolean isGuiScreen = player.openContainer != player.inventoryContainer;
		
		if (!JetpackHelper.isActiveJetpack(player) || player.onGround || isGuiScreen) {
			setJetpackBoostKey(player, false);
			return;
		}
		
		boolean keyJump = getJetpackJumpKey(player);
		boolean keyBoost = getJetpackBoostKey(player);
		
		//浮遊処理
		if (keyJump) {
			player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
			player.fallDistance = 0.0F;
			if (player instanceof EntityPlayerMP) {
				((EntityPlayerMP) player).connection.floatingTickCount = 0;
			}
			
			//エネルギー消費
			JetpackHelper.useJetpack(player);
		}
		
		//ブースト処理
		if (keyBoost && JetpackHelper.isActiveJetpackBoost(player)) {
			
			Vec3d vec3d = player.getLookVec();
			double boost = JETPACK_BOOST;
			player.motionX += vec3d.x * 0.1D + (vec3d.x * 1.5D * boost - player.motionX) * 0.5D;
			player.motionZ += vec3d.z * 0.1D + (vec3d.z * 1.5D * boost - player.motionZ) * 0.5D;
			
			//エネルギー消費
			JetpackHelper.useJetpackBoost(player);
		}
	}	
}

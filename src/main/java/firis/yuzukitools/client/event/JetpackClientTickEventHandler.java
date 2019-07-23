package firis.yuzukitools.client.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import firis.yuzukitools.client.sound.SoundJetpack;
import firis.yuzukitools.common.event.JetpackPlayerTickEventHandler;
import firis.yuzukitools.common.item.YKItemJetpack;
import firis.yuzukitools.common.network.NetworkHandler;
import firis.yuzukitools.common.network.PacketJetpackKeyC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class JetpackClientTickEventHandler {
	
	/**
	 * ジャンプキーの保存
	 */
	public static boolean lastKeyjump = false;
	
	/**
	 * ブーストキーの保存
	 */
	public static boolean lastKeyBoost = false;
	
	@SubscribeEvent
	public static void onClientTickEvent(ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END
				&& Minecraft.getMinecraft().player != null){
			onClientTickEventJetpack(event);
		}
	}
	
	/**
	 * Jetpack処理
	 * @param event
	 */
	protected static void onClientTickEventJetpack(ClientTickEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		//Jetpack装備していない or 地上 or GUIを開いている
		if (!YKItemJetpack.isActiveJetpack(player)
				|| player.onGround
				|| Minecraft.getMinecraft().currentScreen != null) {
			//キー入力がonの場合はリセット
			if (lastKeyjump == true || lastKeyBoost == true) {
				//Server側へjetpackキーの状態を通信
				NetworkHandler.network.sendToServer(
						new PacketJetpackKeyC2S.MessageJetpackKey(0));
			}
			lastKeyjump = false;
			lastKeyBoost = false;
			return;
		}
		
		//キー入力判断（何パターンか判断する方法あり）
		//Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		//Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());
		boolean isChange = false;
		
		boolean keyJump = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
		if (keyJump != lastKeyjump) {
			lastKeyjump = keyJump;
			isChange = true;
		}
		boolean keyBoost = Keyboard.isKeyDown(KeyBindingHandler.jetpackBoost.getKeyCode());
		if (keyBoost != lastKeyBoost) {
			lastKeyBoost = keyBoost;
			isChange = true;
		}
		
		//Server側へjetpackキーの状態を通信
		//ビットを立てて判断する
		if (isChange) {
			int packetMode = 0;
			if (lastKeyjump) {
				packetMode |= 1;
			}
			if (lastKeyBoost) {
				packetMode |= 2;
			}
			NetworkHandler.network.sendToServer(
					new PacketJetpackKeyC2S.MessageJetpackKey(packetMode));
		}
		
		boolean sound = false;
		//Jumpボタン押下かつGUIを開いてない場合処理を行う
		//浮遊用
		if (lastKeyjump) {
			player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
			player.fallDistance = 0.0F;
			sound = true;
		}
		
		//ブーストモード用
		if (lastKeyBoost && YKItemJetpack.isActiveJetpackBoost(player)) {
			Vec3d vec3d = player.getLookVec();
			double boost = JetpackPlayerTickEventHandler.JETPACK_BOOST;
			player.motionX += vec3d.x * 0.1D + (vec3d.x * 1.5D * boost - player.motionX) * 0.5D;
			player.motionZ += vec3d.z * 0.1D + (vec3d.z * 1.5D * boost - player.motionZ) * 0.5D;
			sound = true;
		}
		
		//音を鳴らす
		if (sound) {
			PlayJetpackSound(player);
		}
	}
	
	//Jetpackキー入力保存用
	protected static Map<UUID, ISound> jetpackSound = new HashMap<UUID, ISound>();
		
	/**
	 * ジェットパックの音を鳴らす
	 * @param player
	 */
	public static void PlayJetpackSound(EntityPlayer player) {
		
		boolean playing = false;
		
		//playチェック
		if (jetpackSound.containsKey(player.getUniqueID())) {
			playing = Minecraft.getMinecraft()
					.getSoundHandler()
					.isSoundPlaying(jetpackSound.get(player.getUniqueID()));
		}

		//音を鳴らす
		if (!playing) {
			jetpackSound.put(player.getUniqueID(), new SoundJetpack(player));
			Minecraft.getMinecraft()
			.getSoundHandler()
			.playSound(jetpackSound.get(player.getUniqueID()));
		}
	}
	
}

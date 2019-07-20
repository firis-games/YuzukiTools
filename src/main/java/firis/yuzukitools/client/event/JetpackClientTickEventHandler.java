package firis.yuzukitools.client.event;

import firis.yuzukitools.common.item.YKItemJetpack;
import firis.yuzukitools.common.network.NetworkHandler;
import firis.yuzukitools.common.network.PacketJetpackKeyC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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
	private static boolean lastKeyjump = false;
	
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
		
		//Jetpack装備していない or 地上の場合は何もしない
		if (!YKItemJetpack.isActiveJetpack(player)) {
			if (lastKeyjump == true) {
				//Server側へjetpackキーの状態を通信
				NetworkHandler.network.sendToServer(
						new PacketJetpackKeyC2S.MessageJetpackKey(0));
			}
			lastKeyjump = false;
			return;
		}
		
		//キー入力判断（何パターンか判断する方法あり）
		//Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		//Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());
		boolean keyJump = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
		if (keyJump != lastKeyjump) {
			lastKeyjump = keyJump;
			//Server側へjetpackキーの状態を通信
			NetworkHandler.network.sendToServer(
					new PacketJetpackKeyC2S.MessageJetpackKey(lastKeyjump ? 1 : 0));
		}
		
		//Jumpボタン押下かつGUIを開いてない場合処理を行う
		if (lastKeyjump && Minecraft.getMinecraft().currentScreen == null) {
			if (lastKeyjump) {
				player.motionY = Math.min(player.motionY + 0.15D, 0.5D);
				player.fallDistance = 0.0F;
			}
		}
	}
	
}

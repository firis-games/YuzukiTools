package firis.yuzukitools.client.event;

import org.lwjgl.input.Keyboard;

import firis.yuzukitools.common.network.NetworkHandler;
import firis.yuzukitools.common.network.PacketOpenGuiC2S;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * キーボードイベント
 * @author computer
 *
 */
@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class KeyBindingHandler {

	public static final KeyBinding openGuiBackPack = new KeyBinding("key.open_gui_backpack", Keyboard.KEY_V, "itemGroup.tabYuzukiTools");

	public static final KeyBinding jetpackBoost = new KeyBinding("key.jetpack_boost", Keyboard.KEY_B, "itemGroup.tabYuzukiTools");
	
	/**
	 * キーバインド初期化
	 */
	public static void init() {
		
		ClientRegistry.registerKeyBinding(openGuiBackPack);
		ClientRegistry.registerKeyBinding(jetpackBoost);
		
	}
	
	/**
	 * キー入力イベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onKeyInputEvent(KeyInputEvent event) {
	
		if (openGuiBackPack.isKeyDown()) {
			//Server側へ処理を投げる
			NetworkHandler.network.sendToServer(
					new PacketOpenGuiC2S.MessageOpenGui(ModGuiHandler.BACKPACK_KEY));
		}
	}
	
}

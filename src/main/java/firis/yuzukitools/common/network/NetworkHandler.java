package firis.yuzukitools.common.network;

import firis.yuzukitools.YuzukiTools;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(YuzukiTools.MODID);
	
	public static void init() {
		
		int idx = 1;
		
		network.registerMessage(PacketOpenGuiC2S.class, PacketOpenGuiC2S.MessageOpenGui.class, idx++, Side.SERVER);
		
		//Jetpack
		network.registerMessage(PacketJetpackKeyC2S.class, PacketJetpackKeyC2S.MessageJetpackKey.class, idx++, Side.SERVER);
		
	}
}

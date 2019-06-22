package firis.yuzukitools.client.proxy;

import firis.yuzukitools.client.event.KeyBindingHandler;
import firis.yuzukitools.common.proxy.IProxy;

public class ClientProxy implements IProxy {

	@Override
	public void registerKeyBinding() {
		KeyBindingHandler.init();
	}
	
}

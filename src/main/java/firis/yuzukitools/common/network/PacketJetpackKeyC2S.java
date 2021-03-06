package firis.yuzukitools.common.network;

import firis.yuzukitools.common.event.JetpackPlayerTickEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Jetpack用キー入力処理
 * @author computer
 *
 */
public class PacketJetpackKeyC2S implements IMessageHandler<PacketJetpackKeyC2S.MessageJetpackKey, IMessage> {
	
	@Override
	public IMessage onMessage(MessageJetpackKey message, MessageContext ctx) {
		
		//指定のTileEntityのnetwork連動メソッドを呼び出す
		EntityPlayerMP player = ctx.getServerHandler().player;
		
		boolean jumpFlg = (message.mode & 1) == 1 ? true : false; 
		boolean boostFlg = (message.mode & 2) == 2 ? true : false; 
		
		//キー入力判定
		JetpackPlayerTickEventHandler.setJetpackJumpKey(player, jumpFlg);
		JetpackPlayerTickEventHandler.setJetpackBoostKey(player, boostFlg);
		
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageJetpackKey implements IMessage {
		
		public int mode;
		
		public MessageJetpackKey() {
		}

		public MessageJetpackKey(int mode) {
			this.mode = mode;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			
			//書き込んだ順番で読み込み
			this.mode = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.mode);
		}
	}
}
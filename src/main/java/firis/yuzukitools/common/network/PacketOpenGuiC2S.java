package firis.yuzukitools.common.network;

import firis.yuzukitools.common.item.YKItemBlockBackpack;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 縁結びの輪のGuiOpen処理
 * @author computer
 *
 */
public class PacketOpenGuiC2S implements IMessageHandler<PacketOpenGuiC2S.MessageOpenGui, IMessage> {
	
	@Override
	public IMessage onMessage(MessageOpenGui message, MessageContext ctx) {
		
		//指定のTileEntityのnetwork連動メソッドを呼び出す
		EntityPlayerMP player = ctx.getServerHandler().player;
		
		if (message.guiId == ModGuiHandler.BACKPACK_KEY) {
			//チェストにバックパックがある場合のみバックパックを開く
			YKItemBlockBackpack.openGui(player, player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex()), null);
		}
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageOpenGui implements IMessage {
		
		public int guiId;
		
		public MessageOpenGui() {
		}

		public MessageOpenGui(int guiId) {
			this.guiId = guiId;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			
			//書き込んだ順番で読み込み
			this.guiId = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.guiId);
		}
	}
}
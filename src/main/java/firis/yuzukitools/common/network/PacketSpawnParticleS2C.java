package firis.yuzukitools.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * GuiOpen処理
 * @author computer
 *
 */
public class PacketSpawnParticleS2C implements IMessageHandler<PacketSpawnParticleS2C.MessageSpawnParticle, IMessage> {
	
	@Override
	public IMessage onMessage(MessageSpawnParticle message, MessageContext ctx) {
		
		World world = Minecraft.getMinecraft().world;
		BlockPos pos = message.pos;
		
		//骨粉パーティクルを生成
		if (message.particleNo == 0) {
			int amount = 7;
			for (int i = 0; i < amount; i++) {
	            double d0 = world.rand.nextGaussian() * 1.5D + 5D;
	            double d1 = world.rand.nextGaussian() * 1.5D + 5D;
	            double d2 = world.rand.nextGaussian() * 1.5D + 5D;
	            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, 
	            		(double)((float)pos.getX() + world.rand.nextFloat()),
	            		(double)((float)pos.getY() + world.rand.nextFloat()),
	            		(double)((float)pos.getZ() + world.rand.nextFloat()), 
	            		d0, d1, d2);
				
			}
		}
		
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageSpawnParticle implements IMessage {
		
		public BlockPos pos;
		public int particleNo;
		
		public MessageSpawnParticle() {
		}

		public MessageSpawnParticle(BlockPos pos, int particleNo) {
			this.pos = pos;
			this.particleNo = particleNo;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			//書き込んだ順番で読み込み
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			this.pos = new BlockPos(x, y, z);
			this.particleNo = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.pos.getX());
			buf.writeInt(this.pos.getY());
			buf.writeInt(this.pos.getZ());
			buf.writeInt(this.particleNo);
		}
	}
}
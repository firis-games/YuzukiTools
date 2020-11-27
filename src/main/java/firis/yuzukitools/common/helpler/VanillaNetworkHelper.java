package firis.yuzukitools.common.helpler;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VanillaNetworkHelper {

	/**
	 * TileEntityのパケットを送信する
	 * @param tile
	 */
	public static void sendPacketTileEntity(TileEntity tile) {
		//player listを取得
		World world = tile.getWorld();
		//サーバの場合のみ
		if (!world.isRemote) {
			List<EntityPlayer> list = world.playerEntities;
			Packet<?> pkt = tile.getUpdatePacket();
			tile.markDirty();
			if (pkt != null) {
				for (EntityPlayer player : list) {
					if (distanceXZ(tile.getPos(), player.getPosition(), 64)) {
						EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
						mpPlayer.connection.sendPacket(pkt);
					}
				}
			}
		}
	}
	
	/**
	 * XZ軸座標の距離を判定する
	 * @param pos1
	 * @param pos2
	 * @range range
	 * @return
	 */
	private static boolean distanceXZ(BlockPos pos1, BlockPos pos2, int range) {
		int x = pos1.getX() - pos2.getX();
		int z = pos1.getZ() - pos2.getZ();
		return (x * x + z * z) <= range * range;
	}
	
}

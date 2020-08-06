package firis.yuzukitools.common.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.monster.IMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 呪いの石のイベント
 * @author firis-games
 *
 */
@EventBusSubscriber
public class CursedStoneSpawnHandler {
	
	//ランダム
	private static Random rand = new Random();
	
	// 呪いの石
	protected static Set<TileEntity> tileCursedStone = new HashSet<>();

	/**
	 * 呪い石の対象リストに追加する
	 * 
	 * @param tile
	 */
	public static void AddBlock(TileEntity tile) {
		// 追加
		if (!tile.getWorld().isRemote && !tileCursedStone.contains(tile)) {
			tileCursedStone.add(tile);
		}
	}

	/**
	 * 呪い石の対象リストから削除する
	 * 
	 * @param tile
	 */
	public static void removeBlock(TileEntity tile) {
		// 削除
		if (!tile.getWorld().isRemote && tileCursedStone.contains(tile)) {
			tileCursedStone.remove(tile);
		}
	}
	
	/**
	 * スポーン判定後の移動判定
	 * @param event
	 */
	@SubscribeEvent
	public static void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
		
		//敵性Mob以外は処理しない
		if (!(event.getEntity() instanceof IMob)) return;
		
		// ディメンションも考慮する
		ChunkPos checkChunk = new ChunkPos(event.getEntity().getPosition());
		int dimensionId = event.getWorld().provider.getDimension();

		Iterator<TileEntity> iterator = tileCursedStone.iterator();
		while (iterator.hasNext()) {
			TileEntity tile = iterator.next();
			try {
				ChunkPos tilePos = new ChunkPos(tile.getPos());
				int tileDimensionId = tile.getWorld().provider.getDimension();

				// チャンクの範囲か判断する
				// 5x5チャンク判定
				if (dimensionId == tileDimensionId 
						&& tilePos.x - 2 <= checkChunk.x && checkChunk.x <= tilePos.x + 2
						&& tilePos.z - 2 <= checkChunk.z && checkChunk.z <= tilePos.z + 2) {
					//位置を調整する
					double posX = (double)tile.getPos().getX() + rand.nextDouble() * 6.0D - 3.0D;
					double posY = (double)tile.getPos().getY() + rand.nextDouble() * 2.0D + 1.0D;
					double posZ = (double)tile.getPos().getZ() + rand.nextDouble() * 6.0D - 3.0D;
					event.getEntity().setPosition(posX, posY, posZ);
					break;
				}
			} catch (Exception e) {
			}
		}
	}
	
}

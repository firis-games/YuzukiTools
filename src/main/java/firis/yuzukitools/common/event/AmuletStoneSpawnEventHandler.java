package firis.yuzukitools.common.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 魔除けの石イベント
 * 
 * @author firis-games
 *
 */
@EventBusSubscriber
public class AmuletStoneSpawnEventHandler {

	// スポーン停止エリア
	protected static Set<TileEntity> nonSpawnsArea = new HashSet<>();

	/**
	 * スポーン停止対象に追加する
	 * 
	 * @param tile
	 */
	public static void AddBlock(TileEntity tile) {
		// 追加
		if (!tile.getWorld().isRemote && !nonSpawnsArea.contains(tile)) {
			nonSpawnsArea.add(tile);
		}
	}

	/**
	 * スポーン停止対象から削除する
	 * 
	 * @param tile
	 */
	public static void removeBlock(TileEntity tile) {
		// 削除
		if (!tile.getWorld().isRemote && nonSpawnsArea.contains(tile)) {
			nonSpawnsArea.remove(tile);
		}
	}

	/**
	 * 自然沸きスポーンの判定Event
	 * 
	 * @param envet
	 */
	@SubscribeEvent
	public static void onPotentialSpawnsEvent(WorldEvent.PotentialSpawns event) {

		// 敵性モブ
		if (event.getType() == EnumCreatureType.MONSTER) {
			// ディメンションは無視
			ChunkPos checkChunk = new ChunkPos(event.getPos());
			int dimensionId = event.getWorld().provider.getDimension();

			Iterator<TileEntity> iterator = nonSpawnsArea.iterator();
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
						event.setCanceled(true);
					}
				} catch (Exception e) {
				}
			}
		}
	}
}

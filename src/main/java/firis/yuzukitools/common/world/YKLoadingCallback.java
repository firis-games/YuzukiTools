package firis.yuzukitools.common.world;

import java.util.List;

import com.google.common.collect.ImmutableSetMultimap;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.config.YKConfig;
import firis.yuzukitools.common.world.dimension.DimensionHandler;
import firis.yuzukitools.common.world.dimension.skygarden.SkyGardenManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 永続チャンクロード用
 * @author firis-games
 *
 */
@EventBusSubscriber
public class YKLoadingCallback implements LoadingCallback {

	/**
	 * ワールドがロードされた際にチケットから常時読込チャンクを再設定する
	 * 設定がオフの場合はチケットを破棄する
	 * Ticketはディメンション単位で管理する（現状は空中庭園のみ）
	 */
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		
		//空中庭園のみ処理を実行
		if (world.provider.getDimension() != DimensionHandler.dimensionSkyGarden.getId()) return;

		if (YKConfig.SKY_GARDEN_CHUNK_LOAD) {
			//チケットは1枚のみ想定
			for (Ticket ticket : tickets) {
				if (!YuzukiTools.MODID.equals(ticket.getModId())) continue;
				NBTTagCompound nbt = ticket.getModData();
				if (!nbt.hasKey("dim")) continue;
				int dimId = nbt.getInteger("dim");
				if (dimId != DimensionHandler.dimensionSkyGarden.getId()) continue;
				if (!nbt.hasKey("chunk")) continue;
				NBTTagList nbtList = nbt.getTagList("chunk", 10);
				//チャンクの有効化
				for (int i = 0; i < nbtList.tagCount(); i++) {
					NBTTagCompound chunk = (NBTTagCompound) nbtList.get(i);
					ChunkPos pos = new ChunkPos(chunk.getInteger("x"), chunk.getInteger("z"));
					//有効化
					ForgeChunkManager.forceChunk(ticket, pos);
				}
				break;
			}			
		} else {
			//チケット全部破棄
			for (Ticket ticket : tickets) {
				if (YuzukiTools.MODID.equals(ticket.getModId())) {
					ForgeChunkManager.releaseTicket(ticket);
				}
			}
		}

	}
	
	/**
	 * チャンクの強制ロードを有効化
	 */
	/**
	 * チャンクの強制ロードを設定する
	 * @param world
	 * @param chunk
	 */
	public static void forceChunk(WorldServer world, ChunkPos chunk) {
		
		int dimId = world.provider.getDimension();
		
		//現在読み込まれているチケット
		ImmutableSetMultimap<ChunkPos, Ticket> existingTickets = ForgeChunkManager.getPersistentChunksFor(world);
		
		Ticket modTicket = null;
		
		//チケット探査
		for (Ticket ticket : existingTickets.values()) {
			//MODIDが一致かつNBTあり
			if (ticket.getModId().equals(YuzukiTools.MODID)
					&& ticket.getModData().hasKey("dim")) {
				//ディメンションが一致するか
				NBTTagCompound nbt = ticket.getModData();
				if (nbt.getInteger("dim") == dimId) {
					modTicket = ticket;
					break;					
				}
			}
		}
		
		//チケットがない場合は新規作成
		if (modTicket == null) {
			modTicket = ForgeChunkManager.requestTicket(YuzukiTools.INSTANCE, world, ForgeChunkManager.Type.NORMAL);
			NBTTagCompound nbt = modTicket.getModData();
			nbt.setInteger("dim", dimId);
		}
		
		
		NBTTagCompound nbt = modTicket.getModData();
		
		//チャンクパスの登録
		NBTTagList chunkList = new NBTTagList();
		if (nbt.hasKey("chunk")) {
			chunkList = nbt.getTagList("chunk", 10);
		}
		
		NBTTagCompound chunkNbt = new NBTTagCompound();
		chunkNbt.setInteger("x", chunk.x);
		chunkNbt.setInteger("z", chunk.z);
		
		//チャンクパスを保存
		chunkList.appendTag(chunkNbt);
		nbt.setTag("chunk", chunkList);
		
		//チャンクの常時ロード設定
		ForgeChunkManager.forceChunk(modTicket, chunk);
		
	}
	
	/***
	 * チャンクロード時に常時ロードチャンクはチケット登録する
	 * @param event
	 */
	@SubscribeEvent
	public static void onChunkEvent(ChunkEvent.Load event) {
		
		if (!YKConfig.SKY_GARDEN_CHUNK_LOAD) return;
		
		//空中庭園ディメンション判定
		if (event.getWorld() instanceof WorldServer 
				&& event.getWorld().provider.getDimension() == DimensionHandler.dimensionSkyGarden.getId()) {
			//SkyGardenManager			
			if (SkyGardenManager.getInstance().isChunkFloatingIsland(event.getChunk().getPos())) {
				forceChunk((WorldServer) event.getWorld(), event.getChunk().getPos());
				return;
			}
		}
	}
	

}

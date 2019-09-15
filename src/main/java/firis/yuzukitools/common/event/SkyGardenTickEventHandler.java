package firis.yuzukitools.common.event;

import firis.yuzukitools.common.world.dimension.DimensionHandler;
import firis.yuzukitools.common.world.dimension.skygarden.TeleporterDimension;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class SkyGardenTickEventHandler {

	@SubscribeEvent
	public static void onPlayerTickEvent(PlayerTickEvent event) {
		
		//Server側のみ処理を行う
		if (event.phase == Phase.END && event.side == Side.SERVER) {
			onSkyGardenTickEvent(event);
		}
		
	}

	/**
	 * メイン処理
	 * @param event
	 */
	private static void onSkyGardenTickEvent(PlayerTickEvent event) {

		EntityPlayer player = event.player;
		if (player == null) return;
		if (player.posY >= 0.0) return;
		
		//奈落に突入している場合に処理を行う
		World world = player.getEntityWorld();
		
		//空中庭園のみ処理を行う
		if (world.provider.getDimension() == DimensionHandler.dimensionSkyGarden.getId()) {
			
			//オーバーワールドへ移動
			int dimensionId = DimensionType.OVERWORLD.getId();
			
			WorldServer server = player.getServer().getWorld(dimensionId);
			
			BlockPos bedPos = player.getBedLocation(dimensionId);
	        BlockPos pos = null;
	        if (bedPos != null) {
	        	EntityPlayer.getBedSpawnLocation(server, bedPos, player.isSpawnForced(dimensionId));	        	
	        }
	        if (pos == null) {
	        	pos = server.provider.getRandomizedSpawnPoint();
	        }
	        
	        //落下速度を無効化
	        player.fallDistance = 0.0F;
	        
			player.changeDimension(DimensionType.OVERWORLD.getId(), 
					new TeleporterDimension(server, pos.getX(), pos.getY(), pos.getZ()));
		}
		
	}

}

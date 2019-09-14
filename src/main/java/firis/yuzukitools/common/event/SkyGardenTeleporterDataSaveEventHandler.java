package firis.yuzukitools.common.event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import firis.yuzukitools.common.world.dimension.DimensionHandler;
import firis.yuzukitools.common.world.dimension.skygarden.SkyGardenManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class SkyGardenTeleporterDataSaveEventHandler {

	/**
	 * テレポート座標のロード処理
	 * @param event
	 */
	@SubscribeEvent
	public static void onWorldEventLoad(WorldEvent.Load event) {
		
		if (event.getWorld().isRemote) return;
		
		//空中庭園ディメンションのみ処理を行う
		if (event.getWorld().provider.getDimension() != DimensionHandler.dimensionSkyGarden.getId()) return;
		
		Path worldDictPath = Paths.get(event.getWorld().getSaveHandler().getWorldDirectory().getPath(), 
				event.getWorld().provider.getSaveFolder());
		Path saveFile = Paths.get(worldDictPath.toString(), "teleporter.data");
		
		NBTTagCompound compound = new NBTTagCompound();
		try {
			compound = CompressedStreamTools.read(saveFile.toFile());
		} catch (IOException e) {
		}
		
		//クラスへ反映
		SkyGardenManager.readFromNBT(compound);
	}
	
	
	/**
	 * テレポート座標のセーブ処理
	 * @param event
	 */
	@SubscribeEvent
	public static void onWorldEventSave(WorldEvent.Save event) {

		if (event.getWorld().isRemote) return;

		//空中庭園ディメンションのみ処理を行う
		if (event.getWorld().provider.getDimension() != DimensionHandler.dimensionSkyGarden.getId()) return;
		MinecraftServer server = event.getWorld().getMinecraftServer();
		if (server == null) return;
		
		
		//Dimensionフォルダを取得する
		//Path worldDictPath = Paths.get(DimensionManager.getCurrentSaveRootDirectory().getPath(), event.getWorld().provider.getSaveFolder());
		Path worldDictPath = Paths.get(event.getWorld().getSaveHandler().getWorldDirectory().getPath(), 
				event.getWorld().provider.getSaveFolder());
		//念のためフォルダチェック
		if (!Files.isDirectory(worldDictPath)) {
			return;
		}

		NBTTagCompound compound = SkyGardenManager.writeToNBT();		
		Path saveFile = Paths.get(worldDictPath.toString(), "teleporter.data");
		
		try {
			CompressedStreamTools.safeWrite(compound, saveFile.toFile());
		} catch (IOException e) {
		}		
	}
	
}

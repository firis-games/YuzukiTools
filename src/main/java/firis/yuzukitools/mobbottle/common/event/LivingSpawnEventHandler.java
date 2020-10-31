package firis.yuzukitools.mobbottle.common.event;

import firis.yuzukitools.mobbottle.common.tileentity.FMobBottleSpawnerLogic;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

@EventBusSubscriber
public class LivingSpawnEventHandler {

	/**
	 * スポーン制御
	 * @param event
	 */
	@SubscribeEvent
	public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
		
		//モブボトルプラスからは無条件でスポーンさせる
		if (event.getSpawner() instanceof FMobBottleSpawnerLogic
				&& event.getEntity() instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) event.getEntity();
			if (living.isNotColliding()) {
				event.setResult(Result.ALLOW);
			}
		}
	}
}

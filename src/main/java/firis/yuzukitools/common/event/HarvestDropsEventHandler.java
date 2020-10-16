package firis.yuzukitools.common.event;

import java.util.Set;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class HarvestDropsEventHandler {

	/**
	 * 壊れたスポナーのドロップイベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onHarvestDropsEvent(HarvestDropsEvent event) {
		try {
			//モブスポナーの場合
			if (event.getState().getBlock() == Blocks.MOB_SPAWNER
					&& event.getHarvester() instanceof EntityPlayer
					&& event.getHarvester().getHeldItemMainhand().isEmpty())
			{
				//採掘レベルを確認
				ItemStack harvestToolStack = event.getHarvester().getHeldItemMainhand();
				Set<String> toolClasses = harvestToolStack.getItem().getToolClasses(harvestToolStack);
				//採掘レベル2以上で壊れたスポナーがドロップする
				if (toolClasses != null 
						&& toolClasses.contains("pickaxe") 
						&& harvestToolStack.getItem().getHarvestLevel(harvestToolStack, "pickaxe", event.getHarvester(), event.getState()) >= 2) 
				{
					//壊れたモブスポナードロップ
					event.getDrops().add(new ItemStack(YKBlocks.BROKEN_MOB_SPAWNER));
				}
			}
		} catch (Exception e) {
			YuzukiTools.logger.error(e);
		}
	}
	
}

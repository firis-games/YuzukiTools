package firis.yuzukitools.common.event;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EntityJoinWorldEventHandler {

	/**
	 * 村人の誘導アイテム
	 */
	private static final Set<Item> VILLAGER_TEMPTATION_ITEMS = Sets.newHashSet(Items.EMERALD, Item.getItemFromBlock(Blocks.EMERALD_BLOCK), Item.getItemFromBlock(Blocks.EMERALD_ORE));
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		
		//村人の行動追加
		if (event.getEntity() instanceof EntityVillager) {
			
			//村人誘導アイテム設定
			EntityVillager villager = (EntityVillager) event.getEntity();
			villager.tasks.addTask(3, new EntityAITempt(villager, 1.0D, false, VILLAGER_TEMPTATION_ITEMS));
			
			
		}
	}
	
}

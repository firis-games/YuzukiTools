package firis.yuzukitools.common.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class LootTableLoadEventHandler {

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		
		//読み込まれたjsonファイル
		String json_load = event.getName().toString();
		
		//ボーナスチェストの定義
		String json_check = "minecraft:chests/spawn_bonus_chest";
		
		//読み込んだjsonファイルがボーナスチェストかチェック
		if (json_check.equals(json_load)) {
			
			int weight = 1;
			int quality = 0;
			
			//ボーナスチェストの定義の場合は独自設定を追加する
			//loot_tables.inject.chests.spawn_bonus_chest.jsonを読み込み
			LootEntryTable leTable = new LootEntryTable(
					new ResourceLocation("yuzukitools:inject/chests/spawn_bonus_chest"),
					weight,
					quality,
					new LootCondition[0],
					"yk_spawn_bonus_chest");
			
			LootPool pool = new LootPool(
					new LootEntry[] { leTable },
					new LootCondition[0],
					new RandomValueRange(1),
					new RandomValueRange(0, 1),
					"yk_spawn_bonus_chest");
			
			////ボーナスチェスとの定義を上書きする場合はこっち
			//event.setTable(new LootTable(new LootPool[]{pool}));
			//ボーナスチェストの定義に独自設定を追加する
			event.getTable().addPool(pool);
			
		}
	}
	
}

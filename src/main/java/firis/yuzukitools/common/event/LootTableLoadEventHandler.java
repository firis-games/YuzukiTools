package firis.yuzukitools.common.event;

import java.util.HashMap;
import java.util.Map;

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

	private static final Map<String, String> injectLootTable;
	
	/**
	 * 初期化
	 */
	static {
		injectLootTable = new HashMap<>();
		
		//差し替えLootTable
		//ボーナスチェスト
		injectLootTable.put("minecraft:chests/spawn_bonus_chest",
				"yuzukitools:inject/chests/spawn_bonus_chest");
		
		//にわとりドロップ
		injectLootTable.put("minecraft:entities/chicken",
				"yuzukitools:inject/entities/chicken");
		
		//ブタドロップ
		injectLootTable.put("minecraft:entities/pig",
				"yuzukitools:inject/entities/pig");
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		
		//読み込まれたjsonファイル
		String json_load = event.getName().toString();
		
		
		for (String json_check : injectLootTable.keySet()) {
			
			//一致する場合に追加
			if (json_check.equals(json_load)) {
				
				int weight = 1;
				int quality = 0;
				
				String json_inject = injectLootTable.get(json_check);
				
				//ボーナスチェストの定義の場合は独自設定を追加する
				//loot_tables.inject.chests.spawn_bonus_chest.jsonを読み込み
				LootEntryTable leTable = new LootEntryTable(
						new ResourceLocation(json_inject),
						weight,
						quality,
						new LootCondition[0],
						json_inject.replace("/", "_"));
				
				LootPool pool = new LootPool(
						new LootEntry[] { leTable },
						new LootCondition[0],
						new RandomValueRange(1),
						new RandomValueRange(0, 1),
						json_inject.replace("/", "_"));
				
				////ボーナスチェスとの定義を上書きする場合はこっち
				//event.setTable(new LootTable(new LootPool[]{pool}));
				//ボーナスチェストの定義に独自設定を追加する
				event.getTable().addPool(pool);
				
				break;
			}
		}
	}
	
}

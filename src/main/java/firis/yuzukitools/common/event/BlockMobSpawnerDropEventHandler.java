package firis.yuzukitools.common.event;

import java.util.Set;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class BlockMobSpawnerDropEventHandler {

	/**
	 * 壊れたスポナーのドロップイベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onHarvestDropsEvent(HarvestDropsEvent event) {
		//ドロップ対象チェック
		if (isGetBrokenMobSpawner(event.getState(), event.getHarvester())) {
			//壊れたモブスポナードロップ
			event.getDrops().add(new ItemStack(YKBlocks.BROKEN_MOB_SPAWNER));
		}
	}
	
	/**
	 * 壊れたスポナーの経験値制御
	 * @param event
	 */
	@SubscribeEvent
	public static void onBreakEvent(BreakEvent event) {
		//ドロップ対象チェック
		if (isGetBrokenMobSpawner(event.getState(), event.getPlayer())) {
			//壊れたモブスポナードロップ
			event.setExpToDrop(0);
		}		
	}
	
	
	/**
	 * 壊れたスポナーの取得判定
	 */
	private static boolean isGetBrokenMobSpawner(IBlockState state, EntityPlayer player) {
		try {
			//モブスポナーの場合
			if (state.getBlock() == Blocks.MOB_SPAWNER
					&& player instanceof EntityPlayer
					&& !player.getHeldItemMainhand().isEmpty())
			{
				//採掘レベルを確認
				ItemStack harvestToolStack = player.getHeldItemMainhand();
				Set<String> toolClasses = harvestToolStack.getItem().getToolClasses(harvestToolStack);
				
				//シルクタッチ
				boolean isSilkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, harvestToolStack) > 0;
				
				//採掘レベルが2以上
				boolean isHighPickaxe = toolClasses != null 
						&& toolClasses.contains("pickaxe") 
						&& harvestToolStack.getItem().getHarvestLevel(harvestToolStack, "pickaxe", player, state) >= 2;
						
				//採掘レベル2以上 or シルクタッチつきでドロップ対象とする
				if (isSilkTouch || isHighPickaxe) 
				{
					//壊れたモブスポナードロップ
					return true;
				}
			}
		} catch (Exception e) {
			YuzukiTools.logger.error(e);
		}
		return false;
	}
	
}

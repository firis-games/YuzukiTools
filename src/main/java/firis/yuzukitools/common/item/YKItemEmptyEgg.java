package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * 空のたまご
 * @author firis-games
 *
 */
public class YKItemEmptyEgg extends Item {
	
	/**
	 * コンストラクタ
	 */
	public YKItemEmptyEgg() {
		this.setMaxStackSize(1);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}
	
	/**
	 * ターゲットをクリック
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return procCreateSpawnEgg(stack, player, entity);
    }
	
	/**
	 * ターゲットを右クリック
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return procCreateSpawnEgg(stack, playerIn, target);
    }
	
	/**
	 * スポーンエッグを生成する
	 * @param player
	 * @param target
	 * @return
	 */
	public boolean procCreateSpawnEgg(ItemStack stack, EntityPlayer player, Entity target) {
		//MobId取得
		net.minecraftforge.fml.common.registry.EntityEntry entityEntry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry(target.getClass());
		if (entityEntry != null && EntityList.ENTITY_EGGS.containsKey(entityEntry.getRegistryName())) {
			
			ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
			ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, entityEntry.getRegistryName());
			
			//手持ちに追加
			player.inventory.addItemStackToInventory(spawnEgg);
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
			return true;
			
		}
		return false;
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.empty_egg.info"));
    }
}

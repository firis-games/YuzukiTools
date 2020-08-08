package firis.yuzukitools.common.item;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * 村人転職の書
 * @author firis-games
 *
 */
public class YKItemVillagerJobChange extends Item {
	
	private static Random rand = new Random();
	
	/**
	 * コンストラクタ
	 */
	public YKItemVillagerJobChange() {
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}
	
	/**
	 * ターゲットをクリック
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		//村人の職業変更
        return procJobChange(stack, player, entity);
    }
	
	/**
	 * ターゲットを右クリック
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		//村人の職業変更
        return procJobChange(stack, playerIn, target);
    }
	
	/**
	 * 村人の転職処理
	 * @param player
	 * @param target
	 * @return
	 */
	public boolean procJobChange(ItemStack stack, EntityPlayer player, Entity target) {
		if (target instanceof EntityVillager) {
			VillagerRegistry.setRandomProfession((EntityVillager) target, rand);
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
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.villager_job_change.info"));
    }
}

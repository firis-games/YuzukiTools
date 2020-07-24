package firis.yuzukitools.common.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import firis.core.common.helper.ReflectionHelper;
import firis.yuzukitools.YuzukiTools;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

/**
 * 幸運のナイフ
 * @author firis-games
 *
 */
public class YKItemLuckyKnife extends ItemSword {

	private static Random rand = new Random();
	
	//ナイフの攻撃力
	protected float knifeAttack = 1.0f;
	
	/**
	 * コンストラクタ
	 */
	public YKItemLuckyKnife() {
		super(ToolMaterial.GOLD);
		this.setMaxDamage(64);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}
	
	/**
	 * 確率でドロップアイテムを落とす
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		
		//ドロップ増加のエンチャントレベル取得
		int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("looting"), stack);

		int rate = 30;
		//ドロップ増加で補正(1:10% 2:30% 3:60%)
		for (int i = 1; i <= level; i++) {
			rate += i * 10;
		}
		
		//ドロップ判断
		if (!attacker.world.isRemote && rand.nextInt(100) <= rate) {
			
			//アイテムドロップ処理
			Method method = ReflectionHelper.findMethod(target.getClass(), "getLootTable", "func_184647_J");
			ResourceLocation rlLootTable = null;
			try {
				rlLootTable = (ResourceLocation) method.invoke(target);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
			
			//アイテムをドロップさせる
			if (rlLootTable != null) {
				World world = attacker.world;
				LootContext.Builder ctxBuild = new LootContext.Builder((WorldServer) world);
				
				//Player限定判定追加
				if (attacker instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) attacker;
					ctxBuild.withPlayer(player).withLuck(player.getLuck());
				}
				
				LootTable loottable = world
						.getLootTableManager()
						.getLootTableFromLocation(rlLootTable);
				
				List<ItemStack> resultList = loottable.generateLootForPools(rand, ctxBuild.build());
				for (ItemStack result : resultList) {
					InventoryHelper.spawnItemStack(attacker.world, target.posX, target.posY, target.posZ, result);
				}
			}
		}
		return super.hitEntity(stack, target, attacker);
	}
	
	/**
	 * 修理禁止
	 */
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}
	
	@Override
	public float getAttackDamage() {
        return this.knifeAttack;
    }
	
	/**
	 * 攻撃力制御
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.knifeAttack, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }

}

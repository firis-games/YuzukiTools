package firis.yuzukitools.common.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKItemRedstoneSword extends AbstractEnergyItem {

	/**
     * 1回あたりのエネルギー消費量
     */
    protected int useEnergy = 10;
	
	private final float attackDamage;
	
	/**
	 * コンストラクタ
	 */
	public YKItemRedstoneSword() {
		super(3000);
		
		//攻撃力
		this.attackDamage = 3.0F + ToolMaterial.IRON.getAttackDamage();
		this.setFull3D();
	}
	
	/**
	 * ブロック破壊スピード
	 */
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.WEB) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
		}
	}
	
	/**
	 * 攻撃時のエネルギー消費
	 */
	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		this.extractEnergy(stack, this.useEnergy);
        return true;
    }
	
	/**
     * ブロック破壊時のエネルギー消費
     */
	@Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		this.extractEnergy(stack, this.useEnergy * 2);
        return true;
    }
	
	/**
	 * くもの巣を取得可能
	 */
	@Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock() == Blocks.WEB;
    }
	
	/**
	 * 攻撃力と速度を設定
	 */
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND)
        {
        	//エネルギーを保持している場合のみ攻撃力適応
        	if(this.isEnergyStored(stack)) {
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, 0));
        	} else {
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 0.0D, 0));
        	}
        	multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multimap;
    }

}

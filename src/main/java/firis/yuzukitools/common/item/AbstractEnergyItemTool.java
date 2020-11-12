package firis.yuzukitools.common.item;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractEnergyItemTool extends AbstractEnergyItem {
	
	public static int DEFAULT_TOOL_CAPACITY = 50000;
	
	protected final Set<Block> effectiveBlocks;
    protected float efficiency = 4.0F;
    protected float attackDamage;
    protected float attackSpeed;
    protected Item.ToolMaterial toolMaterial;
        
    protected AbstractEnergyItemTool(Item.ToolMaterial material, Set<Block> effectiveBlocks, int capacity)
    {
        this(0.0F, 0.0F, material, effectiveBlocks, capacity);
    }
    
	/**
	 * コンストラクタ
	 */
	public AbstractEnergyItemTool(float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn, int capacity) {
		
		super(capacity);
		
		//パラメータ設定
        this.toolMaterial = materialIn;
        this.effectiveBlocks = effectiveBlocksIn;
        this.efficiency = materialIn.getEfficiency() + 2.0F;
        this.attackDamage = attackDamageIn + materialIn.getAttackDamage();
        this.attackSpeed = attackSpeedIn;
        this.setFull3D();
        
	}
	
	/**
	 * ブロック破壊速度制御
	 */
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
		float defaultSpeed = 1.0F;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultSpeed;
		
        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return efficiency;
        }
        return this.effectiveBlocks.contains(state.getBlock()) ? this.efficiency : defaultSpeed;
    }
	
	/**
	 * 攻撃時のエネルギー消費
	 */
	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		this.extractEnergy(stack, AbstractEnergyItem.USE_ENERGY * 2);
        return true;
    }
	
	/**
     * ブロック破壊時のエネルギー消費
     */
	@Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    	
		this.extractEnergy(stack, AbstractEnergyItem.USE_ENERGY);
        return true;
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
        		//通常モード
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, 0));
	            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)this.attackSpeed, 0));
        	} else {
        		//エネルギーなし
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 0.0D, 0));
	            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3.5D, 0));
        	}
        }
        return multimap;
    }
	
	/**
	 * HarvestLevel判定
	 */
	@Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @javax.annotation.Nullable net.minecraft.entity.player.EntityPlayer player, @javax.annotation.Nullable IBlockState blockState)
    {
		int defaultLevel = -1;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultLevel;

		return super.getHarvestLevel(stack, toolClass,  player, blockState);
    }
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.redstone_tools.info"));
    	super.addInformation(stack, player, tooltip, advanced);
    }
}

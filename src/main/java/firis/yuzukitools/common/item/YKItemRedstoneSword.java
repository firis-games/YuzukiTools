package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemRedstoneSword extends AbstractEnergyItem {

	private final float attackDamage;
	
	protected static int ACTIVE_ENERGY_RATE = 50;
	
	/**
	 * コンストラクタ
	 */
	public YKItemRedstoneSword() {
		super(AbstractEnergyItemTool.DEFAULT_TOOL_CAPACITY * 2);
		
		//攻撃力
		this.attackDamage = 3.0F + ToolMaterial.IRON.getAttackDamage();
		this.setFull3D();
		
		//シールドモーション
		this.addPropertyOverride(new ResourceLocation("sword_active"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
            	return YKItemRedstoneSword.isSwordActive(stack) ? 1.0F : 0.0F;
            }
        });
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
		extractSwordEnergy(stack, 1);
        return true;
    }
	
	/**
     * ブロック破壊時のエネルギー消費
     */
	@Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		this.extractSwordEnergy(stack, 2);
        return true;
    }
	
	/**
	 * 
	 * @param rate
	 */
	public void extractSwordEnergy(ItemStack stack, int rate) {
		
		if (YKItemRedstoneSword.isSwordActive(stack)) {
			//Activeモード
			this.extractEnergy(stack, AbstractEnergyItem.USE_ENERGY * rate * YKItemRedstoneSword.ACTIVE_ENERGY_RATE);
		} else {
			//通常モード
			this.extractEnergy(stack, AbstractEnergyItem.USE_ENERGY * rate);
		}
		
	}
	
	/**
	 * エネルギー消費
	 */
	@Override
	protected void extractEnergy(ItemStack stack, int energy) {
		super.extractEnergy(stack, energy);
		
		//Activeモードエネルギー不足
		if (!this.isSwordActiveEnergyStored(stack)) {
			this.changeSwordActive(stack, false);
		}
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
        	if(!this.isEnergyStored(stack)) {
        		//エネルギーなし
        		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 0.0D, 0));
        		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5D, 0));
        	} else if(YKItemRedstoneSword.isSwordActive(stack)) {
        		//Activeモード
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage * 4 + 4, 0));	            
	            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 1.0D, 0));
	            } else {
        		//通常モード
	            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, 0));
	            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
        	}
        }
        return multimap;
    }

	
	/**
	 * ソードモード判定
	 * @param stack
	 * @return
	 */
	public static boolean isSwordActive(ItemStack stack) {
		
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("active")) {
    		return false;
    	}
    	boolean active = stack.getTagCompound().getBoolean("active");
    	return active ? true : false;
	}
	
	
	/**
	 * 右クリック設置時等の処理
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		//右クリック時の処理
		itemRightClick(player.getHeldItem(hand));
		
		return EnumActionResult.SUCCESS;
	}
	
	/**
	 * 右クリック処理
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		if (worldIn.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
		//右クリック時の処理
		itemRightClick(playerIn.getHeldItem(handIn));
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	/**
	 * モード切替
	 * @param stack
	 */
	public void itemRightClick(ItemStack stack) {
		boolean active = !isSwordActive(stack);
		if (!this.isSwordActiveEnergyStored(stack)) {
			active = false;
		}
		this.changeSwordActive(stack, active);
	}
	
	/**
	 * Activeモード切替
	 * @param active
	 */
	protected void changeSwordActive(ItemStack stack, boolean active) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
    	}
		nbt.setBoolean("active", active);
		
		stack.setTagCompound(nbt);
	}
	
	/**
	 * Activeモードの残量チェック
	 * @param stack
	 * @return
	 */
	protected boolean isSwordActiveEnergyStored(ItemStack stack) {
		
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return false;
		
		int active_energy = AbstractEnergyItem.USE_ENERGY * YKItemRedstoneSword.ACTIVE_ENERGY_RATE; 
		
		if (capability.getEnergyStored() < active_energy) return false;
		return true;
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    	this.addInformationSword(stack, player, tooltip, advanced);
    	super.addInformation(stack, player, tooltip, advanced);
    }
    
    /**
     * レッドストーンの剣用ツールチップ
     */
    @SideOnly(Side.CLIENT)
    public void addInformationSword(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.redstone_sword.info"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("item.redstone_sword.details"));    	
    }
}

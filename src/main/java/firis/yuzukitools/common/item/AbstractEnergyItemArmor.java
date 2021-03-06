package firis.yuzukitools.common.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.capability.ItemStackEnergyStorageProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractEnergyItemArmor extends ItemArmor implements ISpecialArmor {
	
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	protected int capacity;

	/**
	 * コンストラクタ
	 */
	public AbstractEnergyItemArmor(ArmorMaterial armorMaterial, EntityEquipmentSlot equipmentSlot, int capacity) {
		
		super(armorMaterial, 0, equipmentSlot);
		this.setMaxDamage(0);

		this.capacity = capacity;
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setMaxStackSize(1);
	}
	
	/**
	 * エネルギーを表示
	 */
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		//エネルギーをとりあえず表示する
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return;
		
		Integer energy = capability.getEnergyStored();
		Integer maxEnergy = capability.getMaxEnergyStored();
		
		tooltip.add(AbstractEnergyItem.getEnergyInformation(energy, maxEnergy));
    }
	
	/**
	 * アイテムゲージの表示
	 */
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
	
	/**
	 * アイテムゲージをエネルギーとリンクさせる
	 */
	@Override
    public double getDurabilityForDisplay(ItemStack stack) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return 0.0D;
		
		return 1.0D - (double)capability.getEnergyStored() / (double)capability.getMaxEnergyStored();
    }
	
	
	/**
	 * エネルギー残量チェック
	 * @return
	 */
	protected boolean isEnergyStored(ItemStack stack) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return false;
		
		if (capability.getEnergyStored() == 0) return false;
		
		return true;
	}
	
	/**
	 * エネルギーを消費する
	 * @param stack
	 */
	protected void extractEnergy(ItemStack stack, int energy) {
		IEnergyStorage capability = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (capability == null) return;
		
		capability.extractEnergy(energy, false);
	}
	
    /**
     * ForgeEnergyCapability制御用
     */
    @Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ItemStackEnergyStorageProvider(this.capacity, stack);
    }
    
    
    /**
	 * 防御力設定
	 */
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
    	
    	Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == this.armorType) {
        	if (this.isEnergyStored(stack)) {
        		//通常時
	            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.damageReduceAmount, 0));
	            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.toughness, 0));
        	} else {
        		//エネルギーがない場合
        		multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", 0.5, 0));
	            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", 0, 0));
        	}
        }
        return multimap;
    }
    
	
	/**
     * ダメージ軽減計算用
     * @ISpecialArmor
     */
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		//priority 優先度
		//ratio 軽減率 1で完全にダメージ無効化
		//max 軽減率で最大どれだけのダメージを無効化するか
		//    ここか0なら完全無効でも意味がない
		//ratioが0だとdamageArmorが発生しないのでほぼ誤差レベルの軽減を行う
		ArmorProperties prop = new ArmorProperties(0, 0.0000001, Integer.MAX_VALUE);
		return prop;
	}

	/**
	 * アーマー表示
	 * @ISpecialArmor
	 */
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	/**
	 * ダメージ制御
	 * @ISpecialArmor
	 */
	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		this.extractEnergy(stack, AbstractEnergyItem.USE_ENERGY);
	}
	
	/**
	 * エンチャント可否
	 */
	@Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
	
    /**
     * エネルギーが0%と100%のアイテムを登録する
     */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!this.isInCreativeTab(tab)) return;
        
        ItemStack stackEmpty = new ItemStack(this);
        ItemStack stackFull = new ItemStack(this);
        stackFull.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(this.capacity, false);
        
        items.add(stackEmpty);
        items.add(stackFull);
    }

}

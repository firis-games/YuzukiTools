package firis.yuzukitools.common.item;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Iterators;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

/**
 * ハンマーアックス
 * @author computer
 *
 */
public class YKItemToolHammeraxe extends ItemTool {

	/**
	 * 適正ブロック設定
	 */
	public static final Set<Block> EFFECTIVE_ON = INIT_EFFECTIVE_ON();
	protected static Set<Block> INIT_EFFECTIVE_ON() {
		HashSet<Block> set = Sets.newHashSet();
		Iterators.addAll(set, ItemPickaxe.EFFECTIVE_ON.iterator());
		Iterators.addAll(set, ItemAxe.EFFECTIVE_ON.iterator());
		Iterators.addAll(set, ItemSpade.EFFECTIVE_ON.iterator());
		return set;
	}
	
	/**
	 * 攻撃力と速度を設定（斧と同じ）
	 */
	private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};
	
	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public YKItemToolHammeraxe(ToolMaterial material) {
		super(material, EFFECTIVE_ON);
		
		//HarvestLevel設定
		this.setHarvestLevel("pickaxe", material.getHarvestLevel());
		this.setHarvestLevel("axe", material.getHarvestLevel());
		this.setHarvestLevel("shovel", material.getHarvestLevel());
		
		//耐久度（通常ツールの1.2倍切捨て）
		this.setMaxDamage((int) Math.floor(material.getMaxUses() * 1.2));
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		
		//攻撃力と速度を設定
		this.attackDamage = ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = ATTACK_SPEEDS[material.ordinal()];
	}
	
	/**
	 * ハンマーアックス特殊効果
	 * リーチが+1される
	 */
	protected static final UUID REASH_MODIFIER = UUID.fromString("F9EFCBFE-0D89-4095-9351-F17559737288");
    
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	//リーチを伸ばす
            multimap.put(EntityPlayer.REACH_DISTANCE.getName(), 
            		new AttributeModifier(REASH_MODIFIER, 
            				"Tool modifier", 1.0F, 0));
        }
        return multimap;
    }
	
	/**
	 * 石製品と木製製品の破壊スピード調整
	 */
	@Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        //石製品と木製製品の破壊速度
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
        		&& material != Material.IRON && material != Material.ANVIL && material != Material.ROCK
        		? super.getDestroySpeed(stack, state) : this.efficiency;
    }
	
	/**
	 * アンビルなどの鉄製品の取得判定
	 */
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		
		Block block = blockIn.getBlock();
		int level = block.getHarvestLevel(blockIn);
		
		if (level <= this.toolMaterial.getHarvestLevel()) {
			Material material = blockIn.getMaterial();
			if (material == Material.ROCK
					|| material == Material.IRON
					|| material == Material.ANVIL) {
				return true;
			}
		}
		return false;
	}
}

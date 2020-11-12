package firis.yuzukitools.common.item;

import java.util.UUID;

import com.google.common.collect.Multimap;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class YKItemRedstoneHammeraxe extends AbstractEnergyItemTool {

	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemRedstoneHammeraxe(ToolMaterial material) {
	
		super(material, YKItemToolHammeraxe.EFFECTIVE_ON, AbstractEnergyItemTool.DEFAULT_TOOL_CAPACITY);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);

		//HarvestLevel設定
		this.setHarvestLevel("pickaxe", material.getHarvestLevel());
		this.setHarvestLevel("axe", material.getHarvestLevel());
		this.setHarvestLevel("shovel", material.getHarvestLevel());
		
		this.attackDamage = YKItemToolHammeraxe.ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = YKItemToolHammeraxe.ATTACK_SPEEDS[material.ordinal()];

	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		
		float defaultSpeed = 1.0F;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultSpeed;
		
		//石製品と木製製品の破壊速度
        Material material = state.getMaterial();
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
	
	/**
	 * ハンマーアックス特殊効果
	 * リーチが+1される
	 */
	protected static final UUID REASH_MODIFIER = UUID.fromString("F9EFCBFE-0D89-4095-9351-F17559737288");
    
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	//リーチを伸ばす
            multimap.put(EntityPlayer.REACH_DISTANCE.getName(), 
            		new AttributeModifier(REASH_MODIFIER, 
            				"Tool modifier", 2.0F, 0));
        }
        return multimap;
    }
	
}

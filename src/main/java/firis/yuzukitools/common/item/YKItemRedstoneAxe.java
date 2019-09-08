package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class YKItemRedstoneAxe extends AbstractEnergyItemTool {

	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemRedstoneAxe(ToolMaterial material) {
	
		super(material, ItemAxe.EFFECTIVE_ON, AbstractEnergyItemTool.DEFAULT_TOOL_CAPACITY);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);

		this.setHarvestLevel("axe", material.getHarvestLevel());
		
		this.attackDamage = YKItemToolHammeraxe.ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = YKItemToolHammeraxe.ATTACK_SPEEDS[material.ordinal()];

	}
	
	/**
	 * ブロック破壊スピード制御
	 */
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		
		float defaultSpeed = 1.0F;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultSpeed;
		
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
	
}

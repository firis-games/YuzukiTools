package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class YKItemRedstoneAxe extends AbstractEnergyItemTool {

	/**
	 * 攻撃力と速度を設定（斧と同じ）
	 */
	private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};
	
	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemRedstoneAxe(ToolMaterial material) {
	
		super(material, ItemAxe.EFFECTIVE_ON, 3000);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);

		this.setHarvestLevel("axe", material.getHarvestLevel());
		
		this.attackDamage = ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = ATTACK_SPEEDS[material.ordinal()];

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

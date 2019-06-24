package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class YKItemRedstonePickaxe extends AbstractEnergyItemTool {

	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemRedstonePickaxe(ToolMaterial material) {
	
		super(material, ItemPickaxe.EFFECTIVE_ON, 3000);
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);

		this.setHarvestLevel("pickaxe", material.getHarvestLevel());

	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		
		float defaultSpeed = 1.0F;
		
		//エネルギーがない場合
		if(!this.isEnergyStored(stack)) return defaultSpeed;
		
        Material material = state.getMaterial();
        return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getDestroySpeed(stack, state) : this.efficiency;
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

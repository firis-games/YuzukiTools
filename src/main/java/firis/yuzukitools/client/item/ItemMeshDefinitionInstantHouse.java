package firis.yuzukitools.client.item;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemMeshDefinitionInstantHouse implements ItemMeshDefinition {

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return ItemOverrideListInstantHouse.modelLocation;
	}

}

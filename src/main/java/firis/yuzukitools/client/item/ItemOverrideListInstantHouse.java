package firis.yuzukitools.client.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.instanthouse.InstantHouseManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemOverrideListInstantHouse extends ItemOverrideList {

	public ItemOverrideListInstantHouse(List<ItemOverride> overridesIn) {
		super(overridesIn);
	}
	
	//カスタムModel
	public final static ModelResourceLocation modelLocation = new ModelResourceLocation(YuzukiTools.MODID + ":instant_house", "inventory");
	//元のinstant_houseのモデル
	public final static ModelResourceLocation defModelLocation = new ModelResourceLocation(YuzukiTools.MODID + ":instant_house_def", "inventory");
	
	@Override
	public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
    {
		ItemModelMesher imm = FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher();
		
		IBakedModel model = imm.getModelManager().getModel(defModelLocation);
		
		//カスタムアイコン
		ItemStack icon = InstantHouseManager.getIconItemStack(stack);
		
		if (icon.isEmpty()) {
			int autoRegNo = InstantHouseManager.getAutoRegNo(stack);
			if (0 <= autoRegNo && autoRegNo <= 15) {
				ModelResourceLocation autoRegLoc = new ModelResourceLocation(
						YuzukiTools.MODID + ":cstm/instant_house_" + autoRegNo, "inventory");
				model = imm.getModelManager().getModel(autoRegLoc);
			} else {
				//デフォルト
				model = imm.getModelManager().getModel(defModelLocation);
			}
		} else {
			model = imm.getItemModel(icon);
		}
		return model;
    }

}

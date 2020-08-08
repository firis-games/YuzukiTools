package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import firis.yuzukitools.YuzukiTools;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid="baubles", iface="baubles.api.IBauble")
public class YKItemJetpack extends AbstractEnergyItemArmor implements IItemJetpack, IBauble {
	
	public YKItemJetpack() {
		super(ArmorMaterial.LEATHER, EntityEquipmentSlot.CHEST, 200000);
	}
	
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return YuzukiTools.MODID + ":textures/models/armor/jetpack_1.png";
    }

	/**
	 * baubles用
	 * ジェットパックを胴に装備できるようにする
	 * @param
	 * @return
	 */
	@Override
	@Optional.Method(modid = "baubles")
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}
	
}

package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemShieldSword extends ItemSword {

	/**
	 * コンストラクタ
	 * @param material
	 */
	public YKItemShieldSword(ToolMaterial material) {
		super(material);
		
		//耐久度（通常ツールの1.2倍切捨て）
		this.setMaxDamage((int) Math.floor(material.getMaxUses() * 1.2));
				
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		
		//シールドモーション
		this.addPropertyOverride(new ResourceLocation("shield_blocking"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
	}
	
	/**
	 * シールドアクション
	 */
	@Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }
	
	/**
	 * シールドアクション最大時間設定
	 */
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

	/**
	 * 右クリック時シールドアクション
	 */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	
    	ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
	/**
	 * シールド判定
	 */
	@Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
        return true;
    }
	
}

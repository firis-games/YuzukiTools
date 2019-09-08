package firis.yuzukitools.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemRedstoneShieldSword extends YKItemRedstoneSword {
	
	/**
	 * コンストラクタ
	 */
	public YKItemRedstoneShieldSword() {
		
		super();
		
		//Activeモードを上書き
		this.addPropertyOverride(new ResourceLocation("sword_active"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
            	return 0.0F;
            }
        });
		
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
		if (this.isEnergyStored(stack)) {
	        return 72000;			
		}
		return 0;
    }
	
	/**
	 * 標準の挙動
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.PASS;
	}

	/**
	 * 右クリック時シールドアクション
	 */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	
    	ItemStack itemstack = playerIn.getHeldItem(handIn);
    	if (this.isEnergyStored(itemstack)) {
    		playerIn.setActiveHand(handIn);
    		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    	}
    	return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
	
	/**
	 * シールド判定
	 */
	@Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
		if (!this.isEnergyStored(stack)) {
			//エネルギー0
			return false;
		}
        return true;
    }
	
	@Override
    public int getMaxDamage()
    {
        return 1;
    }
	
	@Override
	public boolean isDamaged(ItemStack stack)
    {
        return true;
    }
	
	@Override
	public void setDamage(ItemStack stack, int damage)
    {
		//damage分のエネルギーを消費する
		extractSwordEnergy(stack, damage);
    }

}

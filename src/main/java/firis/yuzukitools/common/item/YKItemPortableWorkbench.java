package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * 手持ち作業台
 * @author firis-games
 *
 */
public class YKItemPortableWorkbench extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemPortableWorkbench() {
		this.setMaxStackSize(1);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		//GUIを表示する
		playerIn.openGui(YuzukiTools.INSTANCE, ModGuiHandler.PORTABLE_WORKBENCH, playerIn.getEntityWorld(), 0, 0, 0);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn)); 
	}
}

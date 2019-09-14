package firis.yuzukitools.common.item;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.world.dimension.DimensionHandler;
import firis.yuzukitools.common.world.dimension.skygarden.TeleporterSkyGarden;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * 空中庭園の鍵
 * @author computer
 *
 */
public class YKItemSkyGardenKey extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemSkyGardenKey() {
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
		if(world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(handIn));

		//成功時テレポートを行う
		if(world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(handIn));
		
		//アルフヘイムポータルが起動中に転移を行う
		WorldServer server;
		server = player.getServer().getWorld(DimensionHandler.dimensionSkyGarden.getId());
		player.changeDimension(DimensionHandler.dimensionSkyGarden.getId(), new TeleporterSkyGarden(server));
		
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(handIn));
    }
}

package firis.yuzukitools.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.world.dimension.DimensionHandler;
import firis.yuzukitools.common.world.dimension.skygarden.SkyGardenManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 空中庭園の鍵
 * @author computer
 *
 */
public class YKItemSkyGardenKey extends Item implements IItemMetadata {

	/**
	 * コンストラクタ
	 */
	public YKItemSkyGardenKey() {
		
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
		//空中庭園へテレポート
		SkyGardenManager.getInstance().TeleporterSkyGarden(player, player.getHeldItem(handIn).getMetadata());
		
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(handIn));
    }
	
	/**
     * Metadata分のアイテムを登録する
     */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!this.isInCreativeTab(tab)) return;
        
        for (int meta = 0; meta <= this.getMaxMetadata(); meta++) {
        	items.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName() + "_" + Integer.toString(stack.getMetadata());
    }
    
	@Override
	public int getMaxMetadata() {
		return 15;
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.sky_garden_key.info"));
    }
    
    /**
     * エリア内の場合は光る
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
    	EntityPlayer player = YuzukiTools.proxy.getClientPlayer();
    	if (player == null) return false;
    	if (player.getEntityWorld().provider.getDimension()
    			!= DimensionHandler.dimensionSkyGarden.getId()) return false;
    	if (SkyGardenManager.getInstance().isChunkFloatingIsland(
    			new ChunkPos(player.getPosition()), stack.getMetadata())) {
    		return true;
    	}
        return false;
    }
}

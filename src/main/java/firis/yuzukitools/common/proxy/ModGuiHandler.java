package firis.yuzukitools.common.proxy;

import firis.core.common.inventory.CapabilityInventory;
import firis.yuzukitools.client.gui.YKGuiContainerBackpack;
import firis.yuzukitools.client.gui.YKGuiPortableWorkbench;
import firis.yuzukitools.client.gui.YKGuiElectricFurnace;
import firis.yuzukitools.client.gui.YKGuiKitchenGarden;
import firis.yuzukitools.client.gui.YKGuiSolarCharger;
import firis.yuzukitools.common.api.baubles.BaublesHelper;
import firis.yuzukitools.common.container.YKContainerBackpack;
import firis.yuzukitools.common.container.YKContainerElectricFurnace;
import firis.yuzukitools.common.container.YKContainerKitchenGarden;
import firis.yuzukitools.common.container.YKContainerSolarCharger;
import firis.yuzukitools.common.container.YKContainerPortableWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	//バックパック(ブロック)
	public final static int BACKPACK_BLOCK = 0;
	
	//バックパック(アイテム)
	public final static int BACKPACK_ITEM = 1;

	//バックパック(アイテム)
	public final static int BACKPACK_KEY = 2;
	
	//ソーラー充電器
	public final static int SOLAR_CHARGER = 3;
	
	//電気炉
	public final static int ELECTRIC_FURNACE = 4;
	
	//家庭菜園
	public final static int KITCHEN_GARDEN = 5;
	
	//手持ち作業台
	public final static int PORTABLE_WORKBENCH = 6;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory inventory = null;
		EnumHand hand = null;
		
		switch (ID) {
		//バックパック
		case ModGuiHandler.BACKPACK_BLOCK :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKContainerBackpack(inventory, player.inventory);
		
		//バックパック(アイテム)
		case ModGuiHandler.BACKPACK_ITEM :
			hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			inventory = new CapabilityInventory(player.getHeldItem(hand));
			return new YKContainerBackpack(inventory, player.inventory, y);
		
		//バックパック(キー)
		case ModGuiHandler.BACKPACK_KEY :
			inventory = new CapabilityInventory(BaublesHelper.getSlotFromArmorOrBaubles(player));
			return new YKContainerBackpack(inventory, player.inventory);
		
		//ソーラー充電器
		case ModGuiHandler.SOLAR_CHARGER :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKContainerSolarCharger(inventory, player.inventory);
		
		//電気炉
		case ModGuiHandler.ELECTRIC_FURNACE :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKContainerElectricFurnace(inventory, player.inventory);
		
		//家庭菜園
		case ModGuiHandler.KITCHEN_GARDEN :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKContainerKitchenGarden(inventory, player.inventory);
			
		//家庭菜園
		case ModGuiHandler.PORTABLE_WORKBENCH :
			return new YKContainerPortableWorkbench(player.inventory, player.world);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		IInventory inventory = null;
		EnumHand hand = null;
		
		switch (ID) {
		//バックパック
		case ModGuiHandler.BACKPACK_BLOCK :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKGuiContainerBackpack(inventory, player.inventory);
			
		//バックパック(アイテム)
		case ModGuiHandler.BACKPACK_ITEM :
			hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			inventory = new CapabilityInventory(player.getHeldItem(hand));
			return new YKGuiContainerBackpack(inventory, player.inventory, y);
			
		//バックパック(キー)
		case ModGuiHandler.BACKPACK_KEY :
			hand = x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
			inventory = new CapabilityInventory(BaublesHelper.getSlotFromArmorOrBaubles(player));
			return new YKGuiContainerBackpack(inventory, player.inventory);
			
		//ソーラー充電器
		case ModGuiHandler.SOLAR_CHARGER :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKGuiSolarCharger(inventory, player.inventory);
			
		//電気炉
		case ModGuiHandler.ELECTRIC_FURNACE :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKGuiElectricFurnace(inventory, player.inventory);
		
		//家庭菜園
		case ModGuiHandler.KITCHEN_GARDEN :
			inventory = new CapabilityInventory(world.getTileEntity(new BlockPos(x, y, z)));
			return new YKGuiKitchenGarden(inventory, player.inventory);
			
		//家庭菜園
		case ModGuiHandler.PORTABLE_WORKBENCH :
			return new YKGuiPortableWorkbench(player.inventory, player.world);
		}
		
		return null;
	}
}

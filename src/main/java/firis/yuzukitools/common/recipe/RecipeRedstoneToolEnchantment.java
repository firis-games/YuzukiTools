package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeRedstoneToolEnchantment extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe  {

	public final Item redstoneTool;
	public final Item medalItem = YKItems.YUZUKI_MEDAL;
	public final List<ItemStack> catalystStackList;
	public final ItemStack resultStack;
	
	/**
	 * @param redstoneTool
	 */
	public RecipeRedstoneToolEnchantment(Item redstoneTool, Enchantment enchantment, Integer level, ItemStack... catyalistStacks) {
		this.setRegistryName(redstoneTool.getRegistryName().toString() + "_" + enchantment + level.toString());
		this.redstoneTool = redstoneTool;
		
		this.catalystStackList = new ArrayList<>();
		for (ItemStack stack : catyalistStacks) {
			catalystStackList.add(stack.copy());
		}
		
		//エンチャントアイテム生成
		ItemStack toolStack = new ItemStack(this.redstoneTool);
		toolStack.addEnchantment(enchantment, level);
		this.resultStack = toolStack;
		
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		boolean tools = false;
		boolean medal = false;
		
		List<ItemStack> chkCatalystStackList = new ArrayList<>();
		//copy
		for(ItemStack stack : this.catalystStackList) {
			chkCatalystStackList.add(stack.copy());
		};
		
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack.isEmpty()) {
				continue;
			}
			
			//RedstoneTool
			if (stack.getItem() == this.redstoneTool) {
				tools = true;
				continue;
			}
			
			//メダル
			if (stack.getItem() == medalItem) {
				medal = true;
				continue;
			}
			
			//触媒
			Iterator<ItemStack> catalystIterator = chkCatalystStackList.iterator();
			boolean catalystIteratorFlg = false;
			while(catalystIterator.hasNext()) {
				ItemStack catalystStack = catalystIterator.next();
				if (stack.getItem() == catalystStack.getItem()
						&& stack.getMetadata() == catalystStack.getMetadata()) {
					catalystIterator.remove();
					catalystIteratorFlg = true;
					break;
				}
			}
			if (catalystIteratorFlg) {
				continue;
			}
			
			//それ以外
			return false;
		}
		
		if (tools && medal && chkCatalystStackList.size() == 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.resultStack.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}

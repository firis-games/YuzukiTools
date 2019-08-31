package firis.yuzukitools.common.recipe;

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
	public final ItemStack catalystStack;
	public final ItemStack resultStack;
	
	/**
	 * 
	 * @param redstoneTool
	 */
	public RecipeRedstoneToolEnchantment(Item redstoneTool, ItemStack catalystStack, Enchantment enchantment, Integer level) {
		this.setRegistryName(redstoneTool.getRegistryName().toString() + "_" + enchantment + level.toString());
		this.redstoneTool = redstoneTool;
		this.catalystStack = catalystStack;
		
		//エンチャントアイテム生成
		ItemStack toolStack = new ItemStack(this.redstoneTool);
		toolStack.addEnchantment(enchantment, level);
		this.resultStack = toolStack;
		
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		boolean tools = false;
		boolean medal = false;
		boolean catalyst = false;
		
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
			if (stack.getItem() == catalystStack.getItem()
					&& stack.getMetadata() == catalystStack.getMetadata()) {
				catalyst = true;
				continue;
			}
			
			//それ以外
			return false;
		}
		
		if (tools && medal && catalyst) {
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

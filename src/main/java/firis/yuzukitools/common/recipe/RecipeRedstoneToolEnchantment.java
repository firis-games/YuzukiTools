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

	protected final Item redstoneTool;
	
	/**
	 * 
	 * @param redstoneTool
	 */
	public RecipeRedstoneToolEnchantment(Item redstoneTool) {
		this.setRegistryName(redstoneTool.getRegistryName().toString() + "_enchantment");
		this.redstoneTool = redstoneTool;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		boolean tools = false;
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
			
			//触媒
			if (stack.getItem() == YKItems.YUZUKI_MEDAL) {
				catalyst = true;
				continue;
			}
			//それ以外
			return false;
		}
		
		if (tools & catalyst) {
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack toolStack = new ItemStack(this.redstoneTool);
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() == this.redstoneTool) {
				toolStack = stack.copy();
			}
		}
		
		//シルクタッチ
		Enchantment silk_touch = Enchantment.getEnchantmentByLocation("silk_touch");
		toolStack.addEnchantment(silk_touch, 1);
		
		return toolStack;
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

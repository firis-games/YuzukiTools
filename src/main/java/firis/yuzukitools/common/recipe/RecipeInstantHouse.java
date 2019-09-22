package firis.yuzukitools.common.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import firis.yuzukitools.common.instanthouse.InstantHouseManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * インスタントハウスのレシピ
 * @author computer
 *
 */
public class RecipeInstantHouse extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe  {

	public ItemStack resultStack;
	public List<ItemStack> recipes;
	
	/**
	 * インスタントハウス単位で登録を行う
	 */
	public RecipeInstantHouse(ItemStack instantHouse) {
		
		this.setRegistryName(InstantHouseManager.getTemplateName(instantHouse));
		
		this.resultStack = instantHouse;
		this.recipes = InstantHouseManager.getRecipes(instantHouse);
		
	}
	
	/**
	 * レシピチェック
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		//copy
		List<ItemStack> chkList = new ArrayList<>();
		for(ItemStack stack : this.recipes) {
			chkList.add(stack.copy());
		};
		
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack.isEmpty()) {
				continue;
			}
			
			//触媒
			Iterator<ItemStack> chkIterator = chkList.iterator();
			boolean chkFlg = false;
			while(chkIterator.hasNext()) {
				ItemStack chkStack = chkIterator.next();
				if (stack.getItem() == chkStack.getItem()
						&& (stack.getMetadata() == chkStack.getMetadata() 
						|| chkStack.getMetadata() == 32767)) {
					chkIterator.remove();
					chkFlg = true;
					break;
				}
			}
			if (chkFlg) {
				continue;
			}
			
			//それ以外
			return false;
			
		}
		
		if (chkList.size() == 0) {
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

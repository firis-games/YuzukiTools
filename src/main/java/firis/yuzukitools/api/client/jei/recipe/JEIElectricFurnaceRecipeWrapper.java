package firis.yuzukitools.api.client.jei.recipe;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
import firis.yuzukitools.common.tileentity.YKTileElectricFurnace;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class JEIElectricFurnaceRecipeWrapper implements IRecipeWrapper {

	public List<List<ItemStack>> inputs;
	public ItemStack output;
	public Integer burnTime;
	
	/**
	 * コンストラクタ
	 */
	public JEIElectricFurnaceRecipeWrapper(RecipesElectricFurnace recipe) {
		this.inputs = new ArrayList<List<ItemStack>>();
		this.inputs.add(recipe.getInputItemStackList());
		this.output = recipe.getOutputItemStack();
		this.burnTime = recipe.getBurnTime();
	}
	
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
		
	}
	
	/**
	 * レシピ表示時に追加情報を描画する
	 */
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
		Integer energy = burnTime * YKTileElectricFurnace.ENERGY_UNIT;
		
		String strEnergy = NumberFormat.getNumberInstance().format(energy) 
				+ I18n.format("info.energy_unit.name");
		
		//文字を描画
		FontRenderer fontRenderer = minecraft.fontRenderer;
		int stringWidth = fontRenderer.getStringWidth(strEnergy);
		fontRenderer.drawString(strEnergy, recipeWidth - stringWidth, 0, 0xFF808080);
	}

}

package firis.yuzukitools.api.client.jei.electricfurnace;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukitools.common.recipe.RecipesKitchenGarden;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class JEIKitchenGardenRecipeWrapper implements IRecipeWrapper {

	public List<List<ItemStack>> inputs;
	public List<ItemStack> output;
	public Integer progress;
	
	/**
	 * コンストラクタ
	 */
	public JEIKitchenGardenRecipeWrapper(RecipesKitchenGarden recipe) {

		List<ItemStack> tmp;

		//inputアイテム
		//種、土壌、肥料の順で設定する
		this.inputs = new ArrayList<List<ItemStack>>();
		
		//種
		tmp = new ArrayList<ItemStack>();
		tmp.add(recipe.getSeed());
		this.inputs.add(tmp);
		//土壌
		this.inputs.add(recipe.getSoilList());
		//肥料
		this.inputs.add(recipe.getFertilizerList());
		
		//outputアイテム
		this.output = new ArrayList<ItemStack>();
		this.output.addAll(recipe.getHarvestList());
		this.progress = recipe.getProgress();
	}
	
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, output);
		
	}
	
	/**
	 * レシピ表示時に追加情報を描画する
	 */
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
		String strProgress = TextFormatting.DARK_GRAY + 
				"Progress" + " " + 
				NumberFormat.getNumberInstance().format(this.progress) + "tick";
		
		//文字を描画
		FontRenderer fontRenderer = minecraft.fontRenderer;
		int stringWidth = fontRenderer.getStringWidth(strProgress);
		fontRenderer.drawString(strProgress, recipeWidth - stringWidth, 56, 0xFF808080);
	}

}

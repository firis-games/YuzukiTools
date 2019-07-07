package firis.yuzukitools.api.client.jei.electricfurnace;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class JEIElectricFurnaceRecipeCategory implements IRecipeCategory<JEIElectricFurnaceRecipeWrapper> {

	public static final String UID = "yuzukitools.electric_furnace";
	
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	
	protected final IDrawableAnimated animArrow;
	
	private static final ResourceLocation recipe_gui = new ResourceLocation(YuzukiTools.MODID, "textures/gui/jei/jei_electric_furnace.png");
	
	/**
	 * コンストラクタ
	 * @param guiHelper
	 */
	public JEIElectricFurnaceRecipeCategory(IGuiHelper guiHelper) {
		
		//背景設定
		this.background = guiHelper.createDrawable(recipe_gui, 0, 0, 82, 54);
		
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(YKBlocks.ELECTRIC_FURNACE));
		this.localizedName = I18n.format("yuzukitools.jei.electric_furnace.title");
		
		//矢印アニメーション用の描画設定
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(recipe_gui, 82, 14, 24, 17);
		this.animArrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
		
	}
	
	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return this.localizedName;
	}
	
	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public String getModName() {
		return YuzukiTools.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	/**
	 * レシピのItemStackを描画
	 */
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIElectricFurnaceRecipeWrapper recipeWrapper, IIngredients ingredients) {
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		guiItemStacks.init(0, true, 4, 16);
		guiItemStacks.init(1, false, 60, 16);
		guiItemStacks.set(ingredients);

	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {

		//矢印のアニメーション
		this.animArrow.draw(minecraft, 29, 17);
	}

}

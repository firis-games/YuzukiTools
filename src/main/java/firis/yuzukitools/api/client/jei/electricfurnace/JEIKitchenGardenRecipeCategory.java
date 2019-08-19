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

public class JEIKitchenGardenRecipeCategory implements IRecipeCategory<JEIKitchenGardenRecipeWrapper> {

	public static final String UID = "yuzukitools.kitchen_garden";
	
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	
	protected final IDrawableAnimated animArrow;
	
	private static final ResourceLocation recipe_gui = new ResourceLocation(YuzukiTools.MODID, "textures/gui/jei/jei_kitchen_garden.png");
	
	/**
	 * コンストラクタ
	 * @param guiHelper
	 */
	public JEIKitchenGardenRecipeCategory(IGuiHelper guiHelper) {
		
		//背景設定
		this.background = guiHelper.createDrawable(recipe_gui, 0, 0, 110, 54);
		
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(YKBlocks.KITCHEN_GARDEN));
		this.localizedName = I18n.format("yuzukitools.jei.kitchen_garden.title");
		
		//矢印アニメーション用の描画設定
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(recipe_gui, 110, 0, 24, 17);
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
	public void setRecipe(IRecipeLayout recipeLayout, JEIKitchenGardenRecipeWrapper recipeWrapper, IIngredients ingredients) {
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		//input（種、土壌、肥料）
		guiItemStacks.init(0, true, 4, 4);
		guiItemStacks.init(1, true, 4, 32);
		guiItemStacks.init(2, true, 32, 36);
		
		
		//収穫物スロット
		int xBasePos = 56;
		int yBasePos = 0;
		int invX = 3;
		int invY = 3;
		int baseSlot = 3;
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	//登録
            	guiItemStacks.init(slotIndex, false, xPos, yPos);
            }
        }
		
		guiItemStacks.set(ingredients);

	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {

		//矢印のアニメーション
		this.animArrow.draw(minecraft, 29, 5);
	}

}

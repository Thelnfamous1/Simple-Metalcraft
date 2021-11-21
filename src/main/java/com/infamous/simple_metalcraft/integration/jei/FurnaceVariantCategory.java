package com.infamous.simple_metalcraft.integration.jei;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;

/**
 * Copied directly from JEI source code - it's not a part of the API, and I see no need to reinvent
 * a perfectly working wheel
 * @author mezz
 * @param <T>
 */
public abstract class FurnaceVariantCategory<T> implements IRecipeCategory<T> {
	protected static final int INPUT_SLOT = 0;
	protected static final int FUEL_SLOT = 1;
	protected static final int OUTPUT_SLOT = 2;

	protected final IDrawableStatic staticFlame;
	protected final IDrawableAnimated animatedFlame;

	public FurnaceVariantCategory(IGuiHelper guiHelper) {
		this.staticFlame = guiHelper.createDrawable(JEIConstants.RECIPE_GUI_VANILLA, 82, 114, 14, 14);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
	}
}
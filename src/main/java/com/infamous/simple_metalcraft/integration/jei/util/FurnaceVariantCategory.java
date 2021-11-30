package com.infamous.simple_metalcraft.integration.jei.util;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;

/**
 * Mostly copied directly from JEI source code, with modifications for this implementation
 * @author mezz and Thelnfamous1
 * @param <T>
 */
public abstract class FurnaceVariantCategory<T> implements IRecipeCategory<T> {
	protected static final int INPUT_SLOT = 0;
	protected static final int FUEL_SLOT = 1;
	protected static final int OUTPUT_SLOT = 2;

	protected final IDrawableStatic staticFlame;
	protected final IDrawableAnimated animatedFlame;

	public FurnaceVariantCategory(IGuiHelper guiHelper) {
		this.staticFlame = this.createStaticFlame(guiHelper);
		this.animatedFlame = guiHelper.createAnimatedDrawable(this.staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
	}

	protected IDrawableStatic createStaticFlame(IGuiHelper guiHelper) {
		return guiHelper.createDrawable(JEIConstants.RECIPE_GUI_VANILLA, 82, 114, 14, 14);
	}
}
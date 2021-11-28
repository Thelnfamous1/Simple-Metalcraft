package com.infamous.simple_metalcraft.integration.jei.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Copied directly from JEI source code - it's not a part of the API, and I see no need to reinvent
 * a perfectly working wheel
 * @author mezz
 * @param <T>
 */
public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends FurnaceVariantCategory<T> {
	public static final String SMELTING_EXPERIENCE_LOCALIZATION = "gui.jei.category.smelting.experience";
	public static final String SMELTING_TIME_SECONDS_LOCALIZATION = "gui.jei.category.smelting.time.seconds";
	private final IDrawable background;
	private final int regularCookTime;
	private final IDrawable icon;
	private final Component localizedName;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public AbstractCookingCategory(IGuiHelper guiHelper, Block icon, Component localizedName, int regularCookTime) {
		super(guiHelper);
		this.background = guiHelper.createDrawable(JEIConstants.RECIPE_GUI_VANILLA, 0, 114, 82, 54);
		this.regularCookTime = regularCookTime;
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(icon));
		this.localizedName = localizedName;
		this.cachedArrows = CacheBuilder.newBuilder()
			.maximumSize(25)
			.build(new CacheLoader<>() {
				@Override
				public IDrawableAnimated load(Integer cookTime) {
					return guiHelper.drawableBuilder(JEIConstants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
						.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
				}
			});
	}

	protected IDrawableAnimated getArrow(T recipe) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = regularCookTime;
		}
		return this.cachedArrows.getUnchecked(cookTime);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void draw(T recipe, PoseStack poseStack, double mouseX, double mouseY) {
		animatedFlame.draw(poseStack, 1, 20);

		IDrawableAnimated arrow = this.getArrow(recipe);
		arrow.draw(poseStack, 24, 18);

		this.drawExperience(recipe, poseStack, 0);
		this.drawCookTime(recipe, poseStack, 45);
	}

	protected void drawExperience(T recipe, PoseStack poseStack, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			TranslatableComponent experienceString = new TranslatableComponent(SMELTING_EXPERIENCE_LOCALIZATION, experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	protected void drawCookTime(T recipe, PoseStack poseStack, int y) {
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			TranslatableComponent timeString = new TranslatableComponent(SMELTING_TIME_SECONDS_LOCALIZATION, cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
		}
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(INPUT_SLOT, true, 0, 0);
		guiItemStacks.init(OUTPUT_SLOT, false, 60, 18);

		guiItemStacks.set(ingredients);
	}

	@Override
	public boolean isHandled(T recipe) {
		return !recipe.isSpecial();
	}
}
package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.integration.jei.util.ForgingRecipeDisplayData;
import com.infamous.simple_metalcraft.integration.jei.util.JEIConstants;
import com.infamous.simple_metalcraft.integration.jei.util.JEIErrorUtil;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;

/**
 * Majority of this copied directly from JEI source code - it's not a part of the API, and I see no need to reinvent
 * a perfectly working wheel
 * @author mezz
 */
public class ForgingRecipeCategory implements IRecipeCategory<ForgingRecipe> {

	private final IDrawable background;
	private final IDrawable icon;
	//private final LoadingCache<ForgingRecipe, ForgingRecipeDisplayData> cachedDisplayData;
	public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, SMRecipes.FORGING_NAME);

	public ForgingRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(JEIConstants.RECIPE_GUI_VANILLA, 0, 168, 125, 18)
			.addPadding(0, 20, 0, 0)
			.build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.ANVIL));
		/*
		cachedDisplayData = CacheBuilder.newBuilder()
			.maximumSize(25)
			.build(new CacheLoader<>() {
				@Override
				public ForgingRecipeDisplayData load(ForgingRecipe key) {
					return new ForgingRecipeDisplayData();
				}
			});
		 */
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends ForgingRecipe> getRecipeClass() {
		return ForgingRecipe.class;
	}

	@Override
	public Component getTitle() {
		return Blocks.ANVIL.getName();
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
	public void setIngredients(ForgingRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(Arrays.asList(recipe.getIngredient(), recipe.getCatalyst()));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ForgingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 0, 0);
		guiItemStacks.init(1, true, 49, 0);
		guiItemStacks.init(2, false, 107, 0);

		guiItemStacks.set(ingredients);

		/*
		ForgingRecipeDisplayData displayData = cachedDisplayData.getUnchecked(recipe);
		displayData.setCurrentIngredients(guiItemStacks.getGuiIngredients());

		 */
	}

	@Override
	public void draw(ForgingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
		/*
		ForgingRecipeDisplayData displayData = cachedDisplayData.getUnchecked(recipe);
		Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = displayData.getCurrentIngredients();
		if (currentIngredients == null) {
			return;
		}

		ItemStack newLeftStack = currentIngredients.get(0).getDisplayedIngredient();
		ItemStack newRightStack = currentIngredients.get(1).getDisplayedIngredient();

		if (newLeftStack == null || newRightStack == null) {
			return;
		}

		ItemStack lastLeftStack = displayData.getLastLeftStack();
		ItemStack lastRightStack = displayData.getLastRightStack();
		int lastCost = displayData.getLastCost();
		if (lastLeftStack == null || lastRightStack == null
			|| !ItemStack.matches(lastLeftStack, newLeftStack)
			|| !ItemStack.matches(lastRightStack, newRightStack)) {
			lastCost = findLevelsCost(newLeftStack, newRightStack;
			displayData.setLast(newLeftStack, newRightStack, lastCost);
		}

		 */

		int lastCost = recipe.getExperienceCost();

		if (lastCost != 0) {
			String costText = lastCost < 0 ? "err" : Integer.toString(lastCost);
			String text = I18n.get(ForgingRecipe.FORGING_COST_LOCALIZATION, costText);

			Minecraft minecraft = Minecraft.getInstance();
			int mainColor = 0xFF80FF20;
			LocalPlayer player = minecraft.player;
			if (player != null &&
				(lastCost >= 40 || lastCost > player.experienceLevel) &&
				!player.isCreative()) {
				// Show red if the player doesn't have enough levels
				mainColor = 0xFFFF6060;
			}

			drawForgingCost(minecraft, poseStack, text, mainColor);
		}
	}

	private void drawForgingCost(Minecraft minecraft, PoseStack poseStack, String text, int mainColor) {
		int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
		int width = minecraft.font.width(text);
		int x = background.getWidth() - 2 - width;
		int y = 27;

		// TODO 1.13 match the new GuiRepair style
		minecraft.font.draw(poseStack, text, x + 1, y, shadowColor);
		minecraft.font.draw(poseStack, text, x, y + 1, shadowColor);
		minecraft.font.draw(poseStack, text, x + 1, y + 1, shadowColor);
		minecraft.font.draw(poseStack, text, x, y, mainColor);
	}

	public static int findLevelsCost(ItemStack leftStack, ItemStack rightStack) {
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return -1;
		}
		Inventory fakeInventory = new Inventory(player);
		try {
			AnvilMenu repair = new AnvilMenu(0, fakeInventory);
			repair.slots.get(0).set(leftStack);
			repair.slots.get(1).set(rightStack);
			return repair.getCost();
		} catch (RuntimeException e) {
			String left = JEIErrorUtil.getItemStackInfo(leftStack);
			String right = JEIErrorUtil.getItemStackInfo(rightStack);
			SimpleMetalcraft.LOGGER.error("Could not get JEI anvil level cost for: ({} and {}).", left, right, e);
			return -1;
		}
	}

}
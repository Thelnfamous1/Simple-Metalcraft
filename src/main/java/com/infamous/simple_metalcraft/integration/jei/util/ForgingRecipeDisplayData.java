package com.infamous.simple_metalcraft.integration.jei.util;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

import mezz.jei.api.gui.ingredient.IGuiIngredient;

/**
 * Copied directly from JEI source code - it's not a part of the API, and I see no need to reinvent
 * a perfectly working wheel
 * @author mezz
 */
public class ForgingRecipeDisplayData {
	@Nullable
	private Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = null;
	@Nullable
	private ItemStack lastLeftStack;
	@Nullable
	private ItemStack lastRightStack;
	private int lastCost;

	@Nullable
	public Map<Integer, ? extends IGuiIngredient<ItemStack>> getCurrentIngredients() {
		return currentIngredients;
	}

	public void setCurrentIngredients(Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients) {
		this.currentIngredients = currentIngredients;
	}

	@Nullable
	public ItemStack getLastLeftStack() {
		return lastLeftStack;
	}

	@Nullable
	public ItemStack getLastRightStack() {
		return lastRightStack;
	}

	public int getLastCost() {
		return lastCost;
	}

	public void setLast(ItemStack leftStack, ItemStack rightStack, int lastCost) {
		this.lastLeftStack = leftStack;
		this.lastRightStack = rightStack;
		this.lastCost = lastCost;
	}
}
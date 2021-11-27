package com.infamous.simple_metalcraft.crafting.batch;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class BatchCookingRecipe extends AbstractCookingRecipe {

    public BatchCookingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(recipeType, id, group, ingredient, result, experience, cookingTime);
    }
}

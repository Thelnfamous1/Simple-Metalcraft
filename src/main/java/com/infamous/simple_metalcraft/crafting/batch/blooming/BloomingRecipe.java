package com.infamous.simple_metalcraft.crafting.batch.blooming;

import com.infamous.simple_metalcraft.crafting.batch.BatchCookingRecipe;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BloomingRecipe extends BatchCookingRecipe {

    public BloomingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(SMRecipes.Types.BLOOMING, id, group, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(SMItems.BLOOMERY.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SMRecipes.BLOOMING.get();
    }
}

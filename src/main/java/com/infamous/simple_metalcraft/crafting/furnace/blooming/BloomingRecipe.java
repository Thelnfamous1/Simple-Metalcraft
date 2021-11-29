package com.infamous.simple_metalcraft.crafting.furnace.blooming;

import com.infamous.simple_metalcraft.crafting.furnace.SMCookingRecipe;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;
import java.util.Map;

public class BloomingRecipe extends SMCookingRecipe {

    public BloomingRecipe(ResourceLocation id, String group, Map<Ingredient, Integer> ingredients, List<ItemStack> results, float experience, int cookingTime) {
        super(SMRecipes.Types.BLOOMING, id, group, ingredients, results, experience, cookingTime);
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

package com.infamous.simple_metalcraft.crafting.anvil;

import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
import java.util.stream.Stream;

public class ForgingRecipe implements Recipe<Container> {
    protected final Ingredient ingredient;
    protected final Ingredient catalyst;
    protected final ItemStack result;
    protected final ResourceLocation id;

    public ForgingRecipe(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.catalyst = catalyst;
        this.result = result;
    }

    public static Optional<ForgingRecipe> getRecipeFor(ItemStack left, ItemStack right, Level level) {
        return level.getRecipeManager().getRecipeFor(SMRecipes.Types.FORGING, new SimpleContainer(left, right), level);
    }

    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0)) && this.catalyst.test(container.getItem(1));
    }

    public ItemStack assemble(Container container) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int gridWith, int gridHeight) {
        return gridWith * gridHeight >= 2;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public boolean isAdditionIngredient(ItemStack stack) {
        return this.catalyst.test(stack);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.ANVIL);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return SMRecipes.FORGING.get();
    }

    public RecipeType<?> getType() {
        return SMRecipes.Types.FORGING;
    }

    public boolean isIncomplete() {
        return Stream.of(this.ingredient, this.catalyst).anyMatch((i) -> i.getItems().length == 0);
    }

}

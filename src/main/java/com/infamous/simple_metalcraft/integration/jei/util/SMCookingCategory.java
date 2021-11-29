package com.infamous.simple_metalcraft.integration.jei.util;

import com.infamous.simple_metalcraft.crafting.furnace.SMCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SMCookingCategory<T extends SMCookingRecipe> extends AbstractCookingCategory<T>{

    public SMCookingCategory(IGuiHelper guiHelper, Block icon, Component localizedName, int regularCookTime) {
        super(guiHelper, icon, localizedName, regularCookTime);
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        ingredients.setInputLists(
                VanillaTypes.ITEM,
                recipe.getIngredientsMap()
                        .keySet()
                        .stream()
                        .map(ingredient -> Arrays.asList(ingredient.getItems()))
                        .collect(Collectors.toList())
        );
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getResults());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        int inputSlotStart = this.getInputSlotStart();
        int outputSlotStart = this.getOutputSlotStart();

        for(int i = 0; i < this.getNumInputs(); i++){
            guiItemStacks.init(inputSlotStart + i, true, 0, 0);
        }

        for(int i = 0; i < this.getNumOutputs(); i++){
            guiItemStacks.init(outputSlotStart + i, false, 60, 18);
        }

        int inputIndex = this.getInputSlotStart();
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientsMap().entrySet()) {
            if(inputIndex >= this.getNumInputs()){
                break;
            }

            Ingredient ingredient = entry.getKey();
            Integer count = entry.getValue();
            guiItemStacks.set(inputIndex,
                    Arrays.stream(ingredient.getItems())
                            .map(s -> {
                                ItemStack stack = s.copy();
                                stack.setCount(count);
                                return stack;
                            })
                            .collect(Collectors.toList())
            );
            inputIndex++;
        }

        List<ItemStack> results = recipe.getResults();
        for (int outputIndex = 0; outputIndex < results.size(); ++outputIndex) {
            if(outputIndex >= this.getNumOutputs()){
                break;
            }
            guiItemStacks.set(outputSlotStart + outputIndex, results.get(outputIndex));
        }
    }

    protected abstract int getInputSlotStart();

    protected abstract int getOutputSlotStart();

    protected abstract int getNumInputs();

    protected abstract int getNumOutputs();
}

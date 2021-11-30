package com.infamous.simple_metalcraft.integration.jei.util;

import com.infamous.simple_metalcraft.crafting.furnace.SMCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
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
    protected IDrawableStatic createStaticFlame(IGuiHelper guiHelper) {
        return guiHelper.createDrawable(JEIConstants.RECIPE_GUI_CUSTOM, 117, 0, 14, 14);
    }

    @Override
    protected IDrawableAnimated createArrow(Integer cookTime, IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(JEIConstants.RECIPE_GUI_CUSTOM, 117, 14, 24, 17)
                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
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

        final int inputSlotStart = this.getInputSlotStart();
        final int outputSlotStart = this.getOutputSlotStart();

        final int numInputs = this.getNumInputs();

        for(int i = 0; i < numInputs; i++){
            guiItemStacks.init(inputSlotStart + i, true, i * 18, 0);
        }

        for(int i = 0; i < this.getNumOutputs(); i++){
            int inputSlotsOffset = (9 * numInputs) - 9;
            guiItemStacks.init(outputSlotStart + i, false, 60 + inputSlotsOffset + (26 * i), 18);
        }

        int inputIndex = this.getInputSlotStart();
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientsMap().entrySet()) {
            if(inputIndex >= numInputs){
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

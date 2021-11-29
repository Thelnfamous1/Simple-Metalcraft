package com.infamous.simple_metalcraft.crafting.furnace;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface AdvancedCookingRecipe {

    default boolean findMatches(Container container) {
        Map<Ingredient, Integer> foundMatches = new LinkedHashMap<>();
        Map<Ingredient, Integer> ingredients = this.getIngredientsMap();

        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            if(!this.findMatch(container, entry, foundMatches)){
                return false;
            }
        }
        return !ingredients.isEmpty();
    }

    default boolean findMatch(Container container, Map.Entry<Ingredient, Integer> ingredientsEntry, Map<Ingredient, Integer> foundMatches) {
        for (int slotId = 0; slotId < container.getContainerSize(); slotId++) {
            ItemStack stackInSlot = container.getItem(slotId);
            Ingredient key = ingredientsEntry.getKey();
            Integer value = ingredientsEntry.getValue();
            if (!foundMatches.containsKey(key)
                    && key.test(stackInSlot)
                    && stackInSlot.getCount() >= value) {
                foundMatches.put(key, value);
                return true;
            }
        }
        return false;
    }

    default int getBatchSize(Container container){
        Map<Ingredient, Integer> foundMatches = new LinkedHashMap<>();
        Map<Ingredient, Integer> ingredients = this.getIngredientsMap();
        int batchSize = 1;
        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            int matchCount = this.getMatchCount(container, entry, foundMatches);
            if(batchSize > 1){
                batchSize = Math.min(matchCount, batchSize);
            } else{
                batchSize = matchCount;
            }
        }
        return batchSize;
    }

    default int getMatchCount(Container container, Map.Entry<Ingredient, Integer> ingredientsEntry, Map<Ingredient, Integer> foundMatches) {
        for (int slotId = 0; slotId < container.getContainerSize(); slotId++) {
            ItemStack stack = container.getItem(slotId);
            Ingredient key = ingredientsEntry.getKey();
            Integer value = ingredientsEntry.getValue();
            if (!foundMatches.containsKey(key)
                    && key.test(stack)
                    && stack.getCount() >= value) {
                foundMatches.put(key, value);
                return stack.getCount() / value;
            }
        }
        return 0;
    }

    default void consumeInputs(Container container, int batchSize){
        Map<Ingredient, Integer> foundMatches = new LinkedHashMap<>();
        Map<Ingredient, Integer> ingredients = this.getIngredientsMap();
        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            ItemStack match = this.getMatch(container, entry, foundMatches);
            if(!match.isEmpty()){
                match.shrink(entry.getValue() * batchSize);
            }
        }
    }

    default ItemStack getMatch(Container container, Map.Entry<Ingredient, Integer> ingredientsEntry, Map<Ingredient, Integer> foundMatches) {
        for (int slotId = 0; slotId < container.getContainerSize(); slotId++) {
            ItemStack stack = container.getItem(slotId);
            Ingredient key = ingredientsEntry.getKey();
            Integer value = ingredientsEntry.getValue();
            if (!foundMatches.containsKey(key)
                    && key.test(stack)
                    && stack.getCount() >= value) {
                foundMatches.put(key, value);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    Map<Ingredient, Integer> getIngredientsMap();

    List<ItemStack> getResults();

    default ItemStack assemble(int resultIndex) {
       List<ItemStack> results = this.getResults();
       if(resultIndex < results.size()){
          return results.get(resultIndex).copy();
       } else{
          return ItemStack.EMPTY;
       }
    }
}
package com.infamous.simple_metalcraft.crafting;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
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

    default void consumeInputs(Container container){
        Map<Ingredient, Integer> foundMatches = new LinkedHashMap<>();
        Map<Ingredient, Integer> ingredients = this.getIngredientsMap();
        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            ItemStack match = this.getMatch(container, entry, foundMatches);
            if(!match.isEmpty()){
                match.shrink(entry.getValue());
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

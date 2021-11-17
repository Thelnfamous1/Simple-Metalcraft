package com.infamous.simple_metalcraft.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Majority of this was shamelessly copied from SilentChaos512's "Silent Mechanisms" project
 */
public class InventoryHelper {

    public static int getTotalCount(Container container, Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int inputSlotId = 0; inputSlotId < container.getContainerSize(); ++inputSlotId) {
            ItemStack stack = container.getItem(inputSlotId);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    public static boolean findMatches(Map<Ingredient, Integer> ingredients, Container container) {
        for (Ingredient ingredient : ingredients.keySet()) {
            int required = ingredients.get(ingredient);
            int found = InventoryHelper.getTotalCount(container, ingredient);
            if (found < required) {
                return false;
            }
        }

        // Check for non-matching items
        // Note: container.getInputSlots() -> container.getContainerSize, which should work since the
        // Casting Menu and Forging Menu both have containers in the input slot
        for (int inputSlotId = 0; inputSlotId < container.getContainerSize(); ++inputSlotId) {
            ItemStack stack = container.getItem(inputSlotId);
            if (!stack.isEmpty()) {
                boolean foundMatch = false;
                for (Ingredient ingredient : ingredients.keySet()) {
                    if (ingredient.test(stack)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void consumeItems(Container container, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int inputSlotId = 0; inputSlotId < container.getContainerSize(); ++inputSlotId) {
            ItemStack stack = container.getItem(inputSlotId);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    container.setItem(inputSlotId, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }
}
package com.infamous.simple_metalcraft.crafting;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class MetalworkingResultSlot extends Slot {
    private final MetalworkingMenu menu;

    public MetalworkingResultSlot(MetalworkingMenu menu, Container container, int slotId, int slotX, int slotY) {
        super(container, slotId, slotX, slotY);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return this.menu.mayPickup(player, this.hasItem());
    }

    @Override
    public void onTake(Player player, ItemStack stackToTake) {

        if (!player.getAbilities().instabuild) {
            if (this.menu.getXpCost() > 0) {
                player.giveExperienceLevels(-this.menu.getXpCost());
            }
        }

        this.menu.setXpCost(0);

        stackToTake.onCraftedBy(player.level, player, stackToTake.getCount());
        this.menu.resultContainer.awardUsedRecipes(player);

        // Have to consume the correct amount of input items on take
        MetalworkingRecipe recipe = this.menu.getRecipes().get(menu.getSelectedRecipeIndex());
        Map<Ingredient, Integer> ingredientCounts = recipe.getIngredientMap();
        this.consumeInputs(ingredientCounts, this.menu.inputContainer);
      /*
         Added to fix bug with input slots not showing the correct recipes
         with remaining items in input slot not being sufficient for them
      */
        this.menu.slotsChanged(this.menu.inputContainer);
        this.menu.setupResultSlot();

        menu.getAccess().execute((level, blockPos) -> {
            long l = level.getGameTime();
            if (this.menu.lastSoundTime != l) {
                level.playSound((Player) null, blockPos, this.menu.getTakeSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                this.menu.lastSoundTime = l;
            }

        });
        super.onTake(player, stackToTake);
    }

    private void consumeInputs(Map<Ingredient, Integer> ingredientCounts, Container inputContainer) {
        for (Ingredient ingredient : ingredientCounts.keySet()) {
            InventoryHelper.consumeItems(inputContainer, ingredient, ingredientCounts.get(ingredient));
        }
    }

}

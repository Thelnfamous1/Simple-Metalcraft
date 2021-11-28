package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    @Shadow @Final private Player player;
    private static final String REPAIR_COST_LOCALIZATION = "container." + SimpleMetalcraft.MOD_ID + ".repair.cost";
    @Nullable
    private ForgingRecipe cachedForgingRecipe;

    public AnvilScreenMixin(AnvilMenu anvilMenu, Inventory inventory, Component component, ResourceLocation location) {
        super(anvilMenu, inventory, component, location);
    }

    @ModifyVariable(at = @At(value = "STORE", ordinal = 2), ordinal = 0, method = "renderLabels")
    private Component getForgingComponent(Component defaultValue){
        int cost = this.menu.getCost();
        ItemStack left = this.menu.getSlot(0).getItem();
        ItemStack right = this.menu.getSlot(1).getItem();
        if(this.cachedForgingRecipe == null){
            Optional<ForgingRecipe> forgingRecipe = ForgingRecipe.getRecipeFor(left, right, this.player.level);
            forgingRecipe.ifPresent(fr -> this.cachedForgingRecipe = fr);
        }

        if(this.cachedForgingRecipe != null && this.cachedForgingRecipe.matches(new SimpleContainer(left, right), this.player.level)){
            return new TranslatableComponent(ForgingRecipe.FORGING_COST_LOCALIZATION, cost);
        } else {
            this.cachedForgingRecipe = null;
            if(left.isDamageableItem() && left.getItem().isValidRepairItem(left, right)){
                return new TranslatableComponent(REPAIR_COST_LOCALIZATION, cost);
            } else {
                return defaultValue;
            }
        }
    }
}

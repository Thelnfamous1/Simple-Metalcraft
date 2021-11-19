package com.infamous.simple_metalcraft.mixin;

import com.google.common.collect.ImmutableList;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryMenu;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(RecipeBookComponent.class)
public abstract class RecipeBookComponentMixin {

    @Shadow @Nullable private RecipeBookTabButton selectedTab;
    @Shadow
    @Final
    private List<RecipeBookTabButton> tabButtons;

    @Final
    @Shadow
    private RecipeBookPage recipeBookPage;

    @Shadow
    protected RecipeBookMenu<?> menu;

    // Avoids an IndexOutOfBoundsException when tabButtons is empty for the Bloomery recipe book component
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeBookMenu;getRecipeBookCategories()Ljava/util/List;", ordinal = 0), method = "initVisuals", cancellable = true)
    private void handleNull(CallbackInfo ci){
        if(this.tabButtons.isEmpty() && this.menu.getRecipeBookCategories().isEmpty()){
            this.updateCollections(false);
            this.updateTabs();
            ci.cancel();
        }
    }

    // Avoids an NullPointerException when selectedTab is null for the Bloomery recipe book component
    @Inject(at = @At("HEAD"), method = "updateCollections", cancellable = true)
    private void handleNull(boolean p_100383_, CallbackInfo ci){
        if(this.selectedTab == null){
            this.recipeBookPage.updateCollections(ImmutableList.of(), p_100383_);
            ci.cancel();
        }
    }

    @Shadow protected abstract void updateCollections(boolean p_100383_);

    @Shadow
    protected abstract void updateTabs();

}

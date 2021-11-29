package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

    // Shut up the logger spam from this method for custom recipe types
    @Inject(at = @At("HEAD"), method = "getCategory", cancellable = true)
    private static void getCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (recipe.getType() == SMRecipes.Types.BLOOMING
                || recipe.getType() == SMRecipes.Types.CEMENTATION
                || recipe.getType() == SMRecipes.Types.FORGING
                || recipe.getType() == SMRecipes.Types.BLASTING) {
            cir.setReturnValue(RecipeBookCategories.UNKNOWN);
        }
    }
}

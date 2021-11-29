package com.infamous.simple_metalcraft.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {

    @Invoker
    boolean callIsLit();

    @Accessor
    int getLitTime();

    @Accessor
    void setLitTime(int litTime);

    @Accessor
    int getLitDuration();

    @Accessor
    void setLitDuration(int litDuration);

    @Accessor
    int getCookingProgress();

    @Accessor
    void setCookingProgress(int cookingProgress);

    @Accessor
    int getCookingTotalTime();

    @Accessor
    void setCookingTotalTime(int cookingTotalTime);

    @Accessor
    RecipeType<? extends AbstractCookingRecipe> getRecipeType();

    @Invoker
    int callGetTotalCookTime(Level level, RecipeType<? extends AbstractCookingRecipe> recipeType, Container container);
}

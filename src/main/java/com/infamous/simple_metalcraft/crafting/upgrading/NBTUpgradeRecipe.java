package com.infamous.simple_metalcraft.crafting.upgrading;

import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperatorRecipe;
import com.infamous.simple_metalcraft.mixin.UpgradeRecipeAccessor;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;

import java.util.Optional;

/**
 * Credit to Dariensg for their GemSmithingRecipe example
 */
public class NBTUpgradeRecipe extends UpgradeRecipe implements NBTOperatorRecipe {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<NBTOperator> nbtOperator = Optional.empty();

    public NBTUpgradeRecipe(ResourceLocation id, Ingredient base, Ingredient additive, ItemStack result) {
        super(id, base, additive, result);
    }

    @Override
    public boolean hasOperator() {
        return nbtOperator.isPresent();
    }

    @Override
    public NBTOperator getOperator() {
        return nbtOperator.get();
    }

    @Override
    public void setOperator(NBTOperator operator) {
        this.nbtOperator = Optional.of(operator);
    }

    @Override
    public ItemStack assemble(Container container) {
        ItemStack resultCopy = this.getResultItem().copy();
        CompoundTag baseTag = container.getItem(0).getTag();
        if (baseTag != null) {
            this.assembleNBT(resultCopy, baseTag);
        }
        return resultCopy;
    }

    private void assembleNBT(ItemStack resultCopy, CompoundTag baseTag) {
        CompoundTag originalResultTag = resultCopy.getTag();
        if (originalResultTag != null) {
            CompoundTag newResultTag = baseTag.copy();
            if(this.nbtOperator.isPresent()){
                //SimpleMetalcraft.LOGGER.info("Operating on assembled result for recipe {}", this.id);
                resultCopy.setTag(this.nbtOperator.get().operate(newResultTag, originalResultTag.copy()));
            } else{
                resultCopy.setTag(newResultTag); // vanilla behavior
            }
        }
    }

    public RecipeSerializer<?> getSerializer() {
        return SMRecipes.UPGRADING.get();
    }

    public RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    public Ingredient getBase(){
        return ((UpgradeRecipeAccessor)this).getBase();
    }

    public Ingredient getAddition(){
        return ((UpgradeRecipeAccessor)this).getAddition();
    }
}

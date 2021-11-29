package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomingConstants;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.integration.jei.util.SMCookingCategory;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class BloomingRecipeCategory extends SMCookingCategory<BloomingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, SMRecipes.BLOOMING_NAME);

    public BloomingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, SMBlocks.BLOOMERY.get(), BloomingConstants.BLOOMERY_TITLE, SMRecipes.BLOOMERY_COOKING_TIME);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<BloomingRecipe> getRecipeClass() {
        return BloomingRecipe.class;
    }

    @Override
    protected int getInputSlotStart() {
        return BloomingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getOutputSlotStart() {
        return BloomingConstants.SLOT_RESULT_A;
    }

    @Override
    protected int getNumInputs() {
        return BloomingConstants.NUM_INPUT_SLOTS;
    }

    @Override
    protected int getNumOutputs() {
        return BloomingConstants.NUM_OUTPUT_SLOTS;
    }
}

package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastingConstants;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastingRecipe;
import com.infamous.simple_metalcraft.integration.jei.util.SMCookingCategory;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class SMBlastingRecipeCategory extends SMCookingCategory<SMBlastingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, SMRecipes.BLASTING_NAME);

    public SMBlastingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, SMBlocks.BLAST_FURNACE.get(), SMBlastingConstants.BLAST_FURNACE_COMPONENT, SMRecipes.BLAST_FURNACE_COOKING_TIME);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<SMBlastingRecipe> getRecipeClass() {
        return SMBlastingRecipe.class;
    }

    @Override
    protected int getInputSlotStart() {
        return SMBlastingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getOutputSlotStart() {
        return SMBlastingConstants.SLOT_RESULT_A;
    }

    @Override
    protected int getNumInputs() {
        return SMBlastingConstants.NUM_INPUT_SLOTS;
    }

    @Override
    protected int getNumOutputs() {
        return SMBlastingConstants.NUM_OUTPUT_SLOTS;
    }
}

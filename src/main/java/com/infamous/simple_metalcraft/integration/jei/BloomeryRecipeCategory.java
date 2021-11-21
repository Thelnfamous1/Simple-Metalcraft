package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryBlockEntity;
import com.infamous.simple_metalcraft.crafting.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class BloomeryRecipeCategory extends AbstractCookingCategory<BloomingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, "blooming");

    public BloomeryRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, SMBlocks.BLOOMERY.get(), BloomeryBlockEntity.BLOOMERY_TITLE, 200);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends BloomingRecipe> getRecipeClass() {
        return BloomingRecipe.class;
    }
}

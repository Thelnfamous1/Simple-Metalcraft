package com.infamous.simple_metalcraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class SMTags {

    public static final Tags.IOptionalNamedTag<Block> NEEDS_COPPER_TOOL = tagBlockForge("needs_copper_tool");
    public static final Tags.IOptionalNamedTag<Block> NEEDS_BRONZE_TOOL = tagBlockForge("needs_bronze_tool");
    public static final Tags.IOptionalNamedTag<Block> NEEDS_STEEL_TOOL = tagBlockForge("needs_steel_tool");

    public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = tagItemForge("ingots/copper");
    public static final Tags.IOptionalNamedTag<Item> INGOTS_BRONZE = tagItemForge("ingots/bronze");
    public static final Tags.IOptionalNamedTag<Item> INGOTS_STEEL = tagItemForge("ingots/steel");

    private static Tags.IOptionalNamedTag<Item> tagItem(String name)
    {
        return ItemTags.createOptional(new ResourceLocation(SimpleMetalcraft.MOD_ID, name));
    }

    private static Tags.IOptionalNamedTag<Block> tagBlockForge(String name)
    {
        return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
    private static Tags.IOptionalNamedTag<Item> tagItemForge(String name)
    {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}

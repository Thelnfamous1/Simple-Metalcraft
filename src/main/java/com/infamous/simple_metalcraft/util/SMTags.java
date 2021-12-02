package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
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
    public static final Tags.IOptionalNamedTag<Item> WATER_BREATHING_ARMOR = tagItem("water_breathing_armor");

    public static final Tags.IOptionalNamedTag<EntityType<?>> EQUIP_ARMOR = tagEntityType("equip_armor");
    public static final Tags.IOptionalNamedTag<EntityType<?>> EQUIP_WEAPON = tagEntityType("equip_weapon");

    private static Tags.IOptionalNamedTag<Item> tagItem(String name)
    {
        return ItemTags.createOptional(new ResourceLocation(SimpleMetalcraft.MOD_ID, name));
    }

    private static Tags.IOptionalNamedTag<EntityType<?>> tagEntityType(String name)
    {
        return EntityTypeTags.createOptional(new ResourceLocation(SimpleMetalcraft.MOD_ID, name));
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

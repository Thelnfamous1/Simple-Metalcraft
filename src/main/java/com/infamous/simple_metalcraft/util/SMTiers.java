package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.util.SMTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class SMTiers {

    private SMTiers(){}

    /*
        GOLD(0, 32, 12.0F, 0.0F, 22, () -> Ingredient.of(Items.GOLD_INGOT))

        WOOD(0, 59, 2.0F, 0.0F, 15, () -> Ingredient.of(ItemTags.PLANKS))

        STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
     */

    // We want Copper to be slightly stronger than Stone
    public static final ForgeTier COPPER =
            new ForgeTier(1, 171, 10.0F, 1.0F, 18,
                    SMTags.NEEDS_COPPER_TOOL, () -> Ingredient.of(SMTags.INGOTS_COPPER));


    // We want Bronze to be more efficient and enchantable than iron, but less durable
    public static final ForgeTier BRONZE =
            new ForgeTier(2, 210, 8.0F, 2.0F, 16,
                    SMTags.NEEDS_BRONZE_TOOL, () -> Ingredient.of(SMTags.INGOTS_BRONZE));

    /*
        IRON(2, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.IRON_INGOT))
     */

    public static final ForgeTier METEORIC_IRON =
            new ForgeTier(2, 469, 7.0F, 2.0F, 16,
                    BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(SMItems.METEORIC_IRON_INGOT.get()));

    // We want Steel to be between Iron and Diamond
    public static final ForgeTier STEEL =
            new ForgeTier(3, 687, 7.0F, 3.0F, 8,
                    SMTags.NEEDS_STEEL_TOOL, () -> Ingredient.of(SMTags.INGOTS_STEEL));

    // Another tier, with a durability of 1124?

    /*
        DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.DIAMOND))

        NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> return Ingredient.of(Items.NETHERITE_INGOT);
     */

}

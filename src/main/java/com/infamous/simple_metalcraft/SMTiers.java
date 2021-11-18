package com.infamous.simple_metalcraft;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class SMTiers {

    private SMTiers(){}

    /*
        STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
     */

    // We want Copper to be slightly stronger than Stone
    public static final ForgeTier COPPER =
            new ForgeTier(1, 190, 10.0F, 1.5F, 18,
                    SMTags.NEEDS_COPPER_TOOL, () -> Ingredient.of(SMTags.INGOTS_COPPER));


    // We want Bronze to be more efficient and enchantable than iron, but less durable
    public static final ForgeTier BRONZE =
            new ForgeTier(2, 220, 8.0F, 2.0F, 16,
                    SMTags.NEEDS_BRONZE_TOOL, () -> Ingredient.of(SMTags.INGOTS_BRONZE));

    /*
        IRON(2, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.IRON_INGOT))
     */

    // We want Steel to be between Iron and Diamond
    public static final ForgeTier STEEL =
            new ForgeTier(3, 500, 7.0F, 3.0F, 8,
                    SMTags.NEEDS_STEEL_TOOL, () -> Ingredient.of(SMTags.INGOTS_STEEL));

    /*
        DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.DIAMOND))
     */

    /*
    NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> return Ingredient.of(Items.NETHERITE_INGOT);
     */

}

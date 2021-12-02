package com.infamous.simple_metalcraft.crafting.anvil;

import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.TieredAnvilBlockHolder;

import java.util.function.Supplier;

public interface AnvilTier {

    TieredAnvilBlock getAnvil();

    TieredAnvilBlock getChippedAnvil();

    TieredAnvilBlock getDamagedAnvil();

    float getBreakChance();

    String getName();

    class Impl implements AnvilTier{

        public static final float STONE_ANVIL_BREAK_CHANCE = 0.3f;
        public static final AnvilTier STONE
                = new Impl("stone",
                () -> SMBlocks.STONE_ANVILS,
                STONE_ANVIL_BREAK_CHANCE
        );

        public static final float COPPER_ANVIL_BREAK_CHANCE = 0.24f;
        public static final AnvilTier COPPER
                = new Impl("copper",
                () -> SMBlocks.COPPER_ANVILS,
                COPPER_ANVIL_BREAK_CHANCE
        );

        public static final float BRONZE_ANVIL_BREAK_CHANCE = 0.18f;
        public static final AnvilTier BRONZE
                = new Impl("bronze",
                () -> SMBlocks.BRONZE_ANVILS,
                BRONZE_ANVIL_BREAK_CHANCE
        );

        // IRON ANVIL BREAK CHANCE = 0.12f
        public static final float IRON_ANVIL_BREAK_CHANCE = 0.12f;
        public static final AnvilTier IRON
                = new Impl("iron",
                () -> SMBlocks.IRON_ANVILS,
                IRON_ANVIL_BREAK_CHANCE
        );

        public static final float STEEL_ANVIL_BREAK_CHANCE = 0.06f;
        public static final AnvilTier STEEL
                = new Impl("steel",
                () -> SMBlocks.STEEL_ANVILS,
                STEEL_ANVIL_BREAK_CHANCE
        );

        /*
        public static final float DIAMOND_ANVIL_BREAK_CHANCE = 0.03f;
        public static final AnvilTier DIAMOND
                = new Impl("diamond",
                () -> SMBlocks.DIAMOND_ANVILS,
                DIAMOND_ANVIL_BREAK_CHANCE
        );
         */

        public static final float NETHERITE_ANVIL_BREAK_CHANCE = 0.0f;
        public static final AnvilTier NETHERITE
                = new Impl("netherite",
                () -> SMBlocks.NETHERITE_ANVILS,
                NETHERITE_ANVIL_BREAK_CHANCE
        );

        private final String name;
        private final Supplier<TieredAnvilBlockHolder> tabhSupplier;
        private final float breakChance;

        Impl(String name, Supplier<TieredAnvilBlockHolder> tabhSupplier, float breakChance){
            this.name = name;
            this.tabhSupplier = tabhSupplier;
            this.breakChance = breakChance;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public TieredAnvilBlock getAnvil() {
            return this.tabhSupplier.get().getAnvil().get();
        }

        @Override
        public TieredAnvilBlock getChippedAnvil() {
            return this.tabhSupplier.get().getChippedAnvil().get();
        }

        @Override
        public TieredAnvilBlock getDamagedAnvil() {
            return this.tabhSupplier.get().getDamagedAnvil().get();
        }

        @Override
        public float getBreakChance() {
            return this.breakChance;
        }

    }
}

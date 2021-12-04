package com.infamous.simple_metalcraft.worldgen;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OreRegistration {
    public static final int TIN_ORE_VEIN_SIZE = 6;
    public static final int BURIED_TIN_ORE_VEIN_SIZE = 6;
    public static final float BURIED_TIN_ORE_DISCARD_CHANCE = 0.5F;
    public static final int TIN_ORE_VEINS_PER_CHUNK = 4;
    public static final int TIN_ORE_MIN_HEIGHT = -24;
    public static final int TIN_ORE_MAX_HEIGHT = 56;
    public static final int TIN_ORE_LOWER_MIN_HEIGHT = -24;
    public static final int TIN_ORE_LOWER_MAX_HEIGHT = -8;
    public static final int TIN_ORE_LORE_VEINS_PER_CHUNK = 1;
    public static ConfiguredFeature<?, ?> ORE_TIN;
    public static ConfiguredFeature<?, ?> ORE_TIN_BURIED;
    public static PlacedFeature PLACED_ORE_TIN;
    public static PlacedFeature PLACED_ORE_TIN_LOWER;

    private OreRegistration(){
        throw new IllegalStateException("Utility class");
    }

    /*  FOR REFERENCE
        public static final ConfiguredFeature<?, ?> ORE_COPPPER_SMALL =
        FeatureUtils.register("ore_copper_small",
        Feature.ORE.configured(new OreConfiguration(ORE_COPPER_TARGET_LIST, 10)));
        public static final ConfiguredFeature<?, ?> ORE_COPPER_LARGE =
        FeatureUtils.register("ore_copper_large",
        Feature.ORE.configured(new OreConfiguration(ORE_COPPER_TARGET_LIST, 20)));
     */

    public static void registerOreFeatures() {
        List<OreConfiguration.TargetBlockState> tinOreTargets = List.of(
                OreConfiguration
                        .target(OreFeatures.STONE_ORE_REPLACEABLES, SMBlocks.TIN_ORE.get().defaultBlockState()),
                OreConfiguration
                        .target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, SMBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
        );

        ORE_TIN = registerFeature("ore_tin",
                Feature.ORE
                        .configured(new OreConfiguration(tinOreTargets, TIN_ORE_VEIN_SIZE)));
        ORE_TIN_BURIED = registerFeature("ore_tin_buried",
                Feature.ORE.configured(new OreConfiguration(tinOreTargets, BURIED_TIN_ORE_VEIN_SIZE, BURIED_TIN_ORE_DISCARD_CHANCE)));

        PLACED_ORE_TIN = registerPlacedFeature("ore_tin",
                ORE_TIN
                        .placed(commonOrePlacement(TIN_ORE_VEINS_PER_CHUNK, HeightRangePlacement.triangle(VerticalAnchor.absolute(TIN_ORE_MIN_HEIGHT), VerticalAnchor.absolute(TIN_ORE_MAX_HEIGHT)))));
        PLACED_ORE_TIN_LOWER = registerPlacedFeature("ore_tin_lower",
                ORE_TIN_BURIED
                        .placed(orePlacement(CountPlacement.of(UniformInt.of(0, TIN_ORE_LORE_VEINS_PER_CHUNK)), HeightRangePlacement.uniform(VerticalAnchor.absolute(TIN_ORE_LOWER_MIN_HEIGHT), VerticalAnchor.absolute(TIN_ORE_LOWER_MAX_HEIGHT)))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier1) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(count), placementModifier);
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> registerFeature(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, SimpleMetalcraft.withNamespace(name), feature);
    }

    private static PlacedFeature registerPlacedFeature(String name, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, SimpleMetalcraft.withNamespace(name), feature);
    }
}

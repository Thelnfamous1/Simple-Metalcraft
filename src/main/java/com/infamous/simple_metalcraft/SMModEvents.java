package com.infamous.simple_metalcraft;

import com.google.common.collect.ImmutableList;
import com.infamous.simple_metalcraft.crafting.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.casting.CastingRecipe;
import com.infamous.simple_metalcraft.crafting.forging.ForgingRecipe;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = SimpleMetalcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMModEvents {

    /*
        public static final int COPPER_ORE_VEIN_SIZE = 10;
        public static final int COPPER_ORE_VEINS_PER_CHUNK = 6;
        public static final int COPPER_ORE_MIN_HEIGHT = 0;
        public static final int COPPER_ORE_MAX_HEIGHT = 96;

     */
    public static final int TIN_ORE_VEIN_SIZE = 8;
    public static final int TIN_ORE_VEINS_PER_CHUNK = 4;
    public static final int TIN_ORE_MIN_HEIGHT = 0;
    public static final int TIN_ORE_MAX_HEIGHT = 63;

    public static final int PIG_IRON_ORE_VEIN_SIZE_DELTAS = 10;
    public static final int PIG_IRON_ORE_VEIN_SIZE_NETHER = 10;
    public static final int PIG_IRON_ORE_VEINS_PER_CHUNK_DELTAS = 26;
    public static final int PIG_IRON_ORE_VEIN_SIZE_PER_CHUNK_NETHER = 13;

    /*
        public static final ConfiguredFeature<?, ?> ORE_GOLD_DELTAS = register("ore_gold_deltas", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, Features.States.NETHER_GOLD_ORE, 10)).range(Features.Decorators.RANGE_10_10).squared().count(20));
        public static final ConfiguredFeature<?, ?> ORE_GOLD_NETHER = register("ore_gold_nether", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, Features.States.NETHER_GOLD_ORE, 10)).range(Features.Decorators.RANGE_10_10).squared().count(10));
        public static final ConfiguredFeature<?, ?> ORE_QUARTZ_DELTAS = register("ore_quartz_deltas", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, Features.States.NETHER_QUARTZ_ORE, 14)).range(Features.Decorators.RANGE_10_10).squared().count(32));
        public static final ConfiguredFeature<?, ?> ORE_QUARTZ_NETHER = register("ore_quartz_nether", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, Features.States.NETHER_QUARTZ_ORE, 14)).range(Features.Decorators.RANGE_10_10).squared().count(16));
     */

    public static ConfiguredFeature<?, ?> ORE_TIN;

    public static RecipeType<ForgingRecipe> FORGING;
    public static RecipeType<CastingRecipe> CASTING;
    public static RecipeType<BloomingRecipe> BLOOMING;

    private static ImmutableList<OreConfiguration.TargetBlockState> ORE_TIN_TARGET_LIST;

    public static ConfiguredFeature<?, ?> ORE_PIG_IRON_DELTAS;
    public static ConfiguredFeature<?, ?> ORE_PIG_IRON_NETHER;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(
                () -> {
                    registerRecipeTypes();
                    registerFeatures();
                }
        );
    }

    private static void registerFeatures() {
        ORE_TIN_TARGET_LIST =
                ImmutableList.of(
                        OreConfiguration
                        .target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, SMBlocks.TIN_ORE.get().defaultBlockState()),
                        OreConfiguration
                        .target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, SMBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
                );
        ORE_TIN = registerFeature("ore_tin",
                Feature.ORE
                        .configured(new OreConfiguration(ORE_TIN_TARGET_LIST, TIN_ORE_VEIN_SIZE))
                        .rangeTriangle(VerticalAnchor.absolute(TIN_ORE_MIN_HEIGHT), VerticalAnchor.absolute(TIN_ORE_MAX_HEIGHT))
                        .squared()
                        .count(TIN_ORE_VEINS_PER_CHUNK));

        ORE_PIG_IRON_DELTAS = registerFeature("ore_pig_iron_deltas",
                Feature.ORE
                        .configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, SMBlocks.NETHER_PIG_IRON_ORE.get().defaultBlockState(), PIG_IRON_ORE_VEIN_SIZE_DELTAS))
                        .range(Features.Decorators.RANGE_10_10)
                        .squared()
                        .count(PIG_IRON_ORE_VEINS_PER_CHUNK_DELTAS));
        ORE_PIG_IRON_NETHER = registerFeature("ore_pig_iron_nether",
                Feature.ORE
                        .configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, SMBlocks.NETHER_PIG_IRON_ORE.get().defaultBlockState(), PIG_IRON_ORE_VEIN_SIZE_NETHER))
                        .range(Features.Decorators.RANGE_10_10)
                        .squared()
                        .count(PIG_IRON_ORE_VEIN_SIZE_PER_CHUNK_NETHER));
    }

    private static void registerRecipeTypes() {
        CASTING = registerRecipeType(SMRecipes.CASTING_NAME);
        FORGING = registerRecipeType(SMRecipes.FORGING_NAME);
        BLOOMING = registerRecipeType(SMRecipes.BLOOMING_NAME);
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> registerFeature(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, SimpleMetalcraft.MOD_ID + ":" + name, feature);
    }

    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String recipeName) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SimpleMetalcraft.MOD_ID + ":" + recipeName), new RecipeType<T>() {
            public String toString() {
                return recipeName;
            }
        });
    }
}

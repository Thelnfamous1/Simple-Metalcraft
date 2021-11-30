package com.infamous.simple_metalcraft;

import com.infamous.simple_metalcraft.capability.EquipmentCapabilityProvider;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = SimpleMetalcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMModEvents {
    public static final int TIN_ORE_VEIN_SIZE_SMALL = 8;
    public static final int TIN_ORE_VEIN_SIZE_LARGE = 16;
    public static final int TIN_ORE_VEINS_PER_CHUNK = 4;
    public static final int TIN_ORE_MIN_HEIGHT = -24;
    public static final int TIN_ORE_MAX_HEIGHT = 56;

    public static ConfiguredFeature<?, ?> ORE_TIN_SMALL;
    public static ConfiguredFeature<?, ?> ORE_TIN_LARGE;
    public static PlacedFeature PLACED_ORE_TIN_SMALL;
    public static PlacedFeature PLACED_ORE_TIN_LARGE;

    private static List<OreConfiguration.TargetBlockState> ORE_TIN_TARGET_LIST;

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        EquipmentCapabilityProvider.register(event);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(
                () -> {
                    registerRecipeTypes();
                    registerFeatures();
                    registerDispenserBehavior();
                }
        );
    }

    private static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(SMItems.COPPER_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.BRONZE_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.STEEL_SHEARS.get(), new ShearsDispenseItemBehavior());
    }

    /*
        public static final ConfiguredFeature<?, ?> ORE_COPPPER_SMALL =
        FeatureUtils.register("ore_copper_small",
        Feature.ORE.configured(new OreConfiguration(ORE_COPPER_TARGET_LIST, 10)));
        public static final ConfiguredFeature<?, ?> ORE_COPPER_LARGE =
        FeatureUtils.register("ore_copper_large",
        Feature.ORE.configured(new OreConfiguration(ORE_COPPER_TARGET_LIST, 20)));
     */

    private static void registerFeatures() {
        ORE_TIN_TARGET_LIST =
                List.of(
                        OreConfiguration
                        .target(OreFeatures.STONE_ORE_REPLACEABLES, SMBlocks.TIN_ORE.get().defaultBlockState()),
                        OreConfiguration
                        .target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, SMBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())
                );
        ORE_TIN_SMALL = registerFeature("ore_tin_small",
                Feature.ORE
                        .configured(new OreConfiguration(ORE_TIN_TARGET_LIST, TIN_ORE_VEIN_SIZE_SMALL)));
        ORE_TIN_LARGE = registerFeature("ore_tin_large",
                Feature.ORE
                        .configured(new OreConfiguration(ORE_TIN_TARGET_LIST, TIN_ORE_VEIN_SIZE_LARGE)));

        PLACED_ORE_TIN_SMALL = registerPlacedFeature("ore_tin",
                ORE_TIN_SMALL
                        .placed(commonOrePlacement(TIN_ORE_VEINS_PER_CHUNK, HeightRangePlacement.triangle(VerticalAnchor.absolute(TIN_ORE_MIN_HEIGHT), VerticalAnchor.absolute(TIN_ORE_MAX_HEIGHT)))));
        PLACED_ORE_TIN_LARGE = registerPlacedFeature("ore_tin_large",
                ORE_TIN_LARGE
                        .placed(commonOrePlacement(TIN_ORE_VEINS_PER_CHUNK, HeightRangePlacement.triangle(VerticalAnchor.absolute(TIN_ORE_MIN_HEIGHT), VerticalAnchor.absolute(TIN_ORE_MAX_HEIGHT)))));
    }

    private static void registerRecipeTypes() {
        SMRecipes.Types.BLOOMING = registerRecipeType(SMRecipes.BLOOMING_NAME);
        SMRecipes.Types.CEMENTATION = registerRecipeType(SMRecipes.CEMENTATION_NAME);
        SMRecipes.Types.FORGING = registerRecipeType(SMRecipes.FORGING_NAME);
        SMRecipes.Types.BLASTING = registerRecipeType(SMRecipes.BLASTING_NAME);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier1) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(count), placementModifier);
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> registerFeature(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, SimpleMetalcraft.MOD_ID + ":" + name, feature);
    }

    private static PlacedFeature registerPlacedFeature(String name, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, SimpleMetalcraft.MOD_ID + ":" + name, feature);
    }

    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String recipeName) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SimpleMetalcraft.MOD_ID + ":" + recipeName), new RecipeType<T>() {
            public String toString() {
                return recipeName;
            }
        });
    }
}

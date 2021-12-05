package com.infamous.simple_metalcraft.worldgen;

import com.google.common.collect.ImmutableMap;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.mixin.StructureFeatureAccessor;
import com.infamous.simple_metalcraft.mixin.StructureSettingsAccessor;
import com.infamous.simple_metalcraft.registry.SMStructures;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StructureRegistration {

    public static final int METEORITE_STRUCT_SALT = 50630247; // randomly generated from http://www.theminecraftmethod.com/minecraft-seed-generator
    public static final int METEORITE_SPACING = 40; // spacing of 40 chunks on average, same as vanilla Ruined Portal
    public static final float METEORITE_SEPARATION_FACTOR = 0.375F; // comes out to a minimum spacing of 15 chunks, same as vanilla Ruined Portal
    public static StructurePieceType METEORITE;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_STANDARD;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_DESERT;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_JUNGLE;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_SWAMP;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_MOUNTAIN;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_OCEAN;
    public static ConfiguredStructureFeature<MeteoriteConfiguration, ? extends StructureFeature<MeteoriteConfiguration>> METEORITE_END;

    private StructureRegistration(){
        throw new IllegalStateException("Utility class");
    }


    public static void addStructuresToMaps() {
        addToStructureMaps(
                "meteorite",
                SMStructures.METEORITE.get(),
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                buildStructureFeatureConfiguration(
                        METEORITE_SPACING, /* average distance apart in chunks between spawn attempts */
                        METEORITE_SEPARATION_FACTOR, /* fraction of spacing to determine minimum distance apart in chunks between spawn attempts */
                        METEORITE_STRUCT_SALT /* modifies the seed of the structure so no two structures always spawn over each other. Make this large and unique. */
                ));
    }

    @NotNull
    private static StructureFeatureConfiguration buildStructureFeatureConfiguration(int spacing, float separationFactor, int salt) {
        return new StructureFeatureConfiguration(spacing, (int) (spacing * separationFactor), salt);
    }

    public static void registerStructureFeatures() {
        METEORITE_STANDARD = registerStructureFeature("meteorite", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.STANDARD)));
        METEORITE_DESERT = registerStructureFeature("meteorite_desert", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.DESERT)));
        METEORITE_JUNGLE = registerStructureFeature("meteorite_jungle", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.JUNGLE)));
        METEORITE_SWAMP = registerStructureFeature("meteorite_swamp", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.SWAMP)));
        METEORITE_MOUNTAIN = registerStructureFeature("meteorite_mountain", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.MOUNTAIN)));
        METEORITE_OCEAN = registerStructureFeature("meteorite_ocean", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.OCEAN)));
        METEORITE_END = registerStructureFeature("meteorite_end", SMStructures.METEORITE.get().configured(new MeteoriteConfiguration(MeteoriteFeature.Type.END)));
    }

    private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> ConfiguredStructureFeature<FC, F> registerStructureFeature(String name, ConfiguredStructureFeature<FC, F> structureFeature) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, SimpleMetalcraft.withNamespace(name), structureFeature);
    }

    public static void registerStructurePieceTypes() {
        METEORITE = setTemplatePieceId(MeteoritePiece::new, "meteorite");
    }

    private static StructurePieceType setFullContextPieceId(StructurePieceType pieceType, String name) {
        return Registry.register(Registry.STRUCTURE_PIECE, SimpleMetalcraft.withNamespace(name.toLowerCase(Locale.ROOT)), pieceType);
    }

    private static StructurePieceType setTemplatePieceId(StructurePieceType.StructureTemplateType templateType, String name) {
        return setFullContextPieceId(templateType, name);
    }

    static <P extends StructureProcessor> StructureProcessorType<P> registerStructureProcessor(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, SimpleMetalcraft.withNamespace(name), () -> codec);
    }

    // From TelepathicGrunt's RepurposedStructures
    public static <F extends StructureFeature<?>> void addToStructureMaps(String name, F structure, GenerationStep.Decoration stage, StructureFeatureConfiguration structureFeatureConfiguration) {

        // Add to the structures registry
        StructureFeature.STRUCTURES_REGISTRY
                .put(SimpleMetalcraft.withNamespace(name), structure);

        // Add to the STEP map
        StructureFeatureAccessor.simple_metalcraft_getSTEP()
                .put(structure, stage);

        // Add to the DEFAULTS map
        ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> rebuiltDefaults =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();
        StructureSettingsAccessor.simple_metalcraft_setDEFAULTS(rebuiltDefaults);

        // Add to Noise Generator Settings
        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();
            if(structureMap instanceof ImmutableMap){
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureFeatureConfiguration);
                ((StructureSettingsAccessor)settings.getValue().structureSettings()).simple_metalcraft_setStructureConfig(tempMap);
            }
            else{
                structureMap.put(structure, structureFeatureConfiguration);
            }
        });
    }
}

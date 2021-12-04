package com.infamous.simple_metalcraft.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

// From TelepathicGrunt's RepurposedStructures
@Mixin(StructureSettings.class)
public interface StructureSettingsAccessor {


    @Mutable
    @Accessor("DEFAULTS")
    static void simple_metalcraft_setDEFAULTS(ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> defaults) {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor("structureConfig")
    void simple_metalcraft_setStructureConfig(Map<StructureFeature<?>, StructureFeatureConfiguration> newConfig);

    @Final
    @Accessor("configuredStructures")
    ImmutableMap<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> simple_metalcraft_getConfiguredStructures();

    @Mutable
    @Final
    @Accessor("configuredStructures")
    void simple_metalcraft_setConfiguredStructures(ImmutableMap<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> newConfiguredStructures);
}

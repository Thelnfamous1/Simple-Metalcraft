package com.infamous.simple_metalcraft.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// From TelepathicGrunt's RepurposedStructures
@Mixin(StructureSettings.class)
public interface StructureSettingsAccessor {


    @Mutable
    @Accessor("DEFAULTS")
    static void setDEFAULTS(ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> defaults) {
        throw new UnsupportedOperationException();
    }

}

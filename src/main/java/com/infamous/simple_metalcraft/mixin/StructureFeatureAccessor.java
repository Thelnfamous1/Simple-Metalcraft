package com.infamous.simple_metalcraft.mixin;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

// From TelepathicGrunt's RepurposedStructures
@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {

    @Accessor
    static Map<StructureFeature<?>, GenerationStep.Decoration> getSTEP() {
        return null;
    }
}

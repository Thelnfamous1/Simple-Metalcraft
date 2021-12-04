package com.infamous.simple_metalcraft.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

// From TelepathicGrunt's RepurposedStructures
@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {

    @Invoker("codec")
    Codec<ChunkGenerator> simple_metalcraft_getCodec();
}
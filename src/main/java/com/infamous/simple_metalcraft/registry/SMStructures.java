package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.worldgen.MeteoriteConfiguration;
import com.infamous.simple_metalcraft.worldgen.MeteoriteFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SimpleMetalcraft.MOD_ID);

    public static final RegistryObject<StructureFeature<MeteoriteConfiguration>> METEORITE = STRUCTURE_FEATURES.register(
            "Ruined_Portal",
            () -> new MeteoriteFeature(MeteoriteConfiguration.CODEC)
    );
}

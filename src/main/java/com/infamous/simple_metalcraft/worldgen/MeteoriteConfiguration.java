package com.infamous.simple_metalcraft.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MeteoriteConfiguration implements FeatureConfiguration {
   public static final Codec<MeteoriteConfiguration> CODEC = MeteoriteFeature.Type.CODEC
           .fieldOf("meteorite_type")
           .xmap(MeteoriteConfiguration::new, (mc) -> mc.meteoriteType)
           .codec();
   public final MeteoriteFeature.Type meteoriteType;

   public MeteoriteConfiguration(MeteoriteFeature.Type type) {
      this.meteoriteType = type;
   }
}
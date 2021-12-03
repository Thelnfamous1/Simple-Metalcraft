package com.infamous.simple_metalcraft.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class MeteoriteFeature extends StructureFeature<MeteoriteConfiguration> {
   private static final String[] STRUCTURE_LOCATION_METEORITES =
           new String[]{
                   "meteorite/meteorite_1",
                   "meteorite/meteorite_2",
                   "meteorite/meteorite_3",
                   "meteorite/meteorite_4",
                   "meteorite/meteorite_5",
                   "meteorite/meteorite_6",
                   "meteorite/meteorite_7",
                   "meteorite/meteorite_8",
                   "meteorite/meteorite_9",
                   "meteorite/meteorite_10"
            };
   private static final String[] STRUCTURE_LOCATION_GIANT_METEORITES =
           new String[]{
                   "meteorite/giant_meteorite_1",
                   "meteorite/giant_meteorite_2",
                   "meteorite/giant_meteorite_3"
            };
   private static final float PROBABILITY_OF_GIANT_METEORITE = 0.05F;
   private static final float PROBABILITY_OF_AIR_POCKET = 0.5F;
   private static final float PROBABILITY_OF_UNDERGROUND = 0.5F;
   private static final float UNDERWATER_MOSSINESS = 0.8F;
   private static final float JUNGLE_MOSSINESS = 0.8F;
   private static final float SWAMP_MOSSINESS = 0.5F;
   private static final int MIN_Y_INDEX = 15;

   public MeteoriteFeature(Codec<MeteoriteConfiguration> codec) {
      super(codec, MeteoriteFeature::pieceGeneratorSupplier);
   }

   private static Optional<PieceGenerator<MeteoriteConfiguration>> pieceGeneratorSupplier(PieceGeneratorSupplier.Context<MeteoriteConfiguration> pieceGeneratorSupplier$context) {
      MeteoritePiece.Properties meteoritePiece$properties = new MeteoritePiece.Properties();
      MeteoriteConfiguration meteoriteConfiguration = pieceGeneratorSupplier$context.config();
      WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
      worldgenRandom.setLargeFeatureSeed(pieceGeneratorSupplier$context.seed(), pieceGeneratorSupplier$context.chunkPos().x, pieceGeneratorSupplier$context.chunkPos().z);
      MeteoritePiece.VerticalPlacement meteoritePiece$verticalPlacement;
      if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.DESERT) {
         meteoritePiece$verticalPlacement = MeteoritePiece.VerticalPlacement.PARTLY_BURIED;
         meteoritePiece$properties.airPocket = false;
         meteoritePiece$properties.mossiness = 0.0F;
      } else if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.JUNGLE) {
         meteoritePiece$verticalPlacement = MeteoritePiece.VerticalPlacement.ON_LAND_SURFACE;
         meteoritePiece$properties.airPocket = worldgenRandom.nextFloat() < PROBABILITY_OF_AIR_POCKET;
         meteoritePiece$properties.mossiness = JUNGLE_MOSSINESS;
         meteoritePiece$properties.overgrown = true;
         meteoritePiece$properties.vines = true;
      } else if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.SWAMP) {
         meteoritePiece$verticalPlacement = MeteoritePiece.VerticalPlacement.ON_OCEAN_FLOOR;
         meteoritePiece$properties.airPocket = false;
         meteoritePiece$properties.mossiness = SWAMP_MOSSINESS;
         meteoritePiece$properties.vines = true;
      } else if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.MOUNTAIN) {
         boolean flag = worldgenRandom.nextFloat() < 0.5F;
         meteoritePiece$verticalPlacement = flag ? MeteoritePiece.VerticalPlacement.IN_MOUNTAIN : MeteoritePiece.VerticalPlacement.ON_LAND_SURFACE;
         meteoritePiece$properties.airPocket = flag || worldgenRandom.nextFloat() < PROBABILITY_OF_AIR_POCKET;
      } else if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.OCEAN) {
         meteoritePiece$verticalPlacement = MeteoritePiece.VerticalPlacement.ON_OCEAN_FLOOR;
         meteoritePiece$properties.airPocket = false;
         meteoritePiece$properties.mossiness = UNDERWATER_MOSSINESS;
      } else if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.END) {
         meteoritePiece$verticalPlacement = MeteoritePiece.VerticalPlacement.IN_END;
         meteoritePiece$properties.airPocket = worldgenRandom.nextFloat() < PROBABILITY_OF_AIR_POCKET;
         meteoritePiece$properties.mossiness = 0.0F;
         meteoritePiece$properties.replaceWithBlackstone = true;
      } else {
         boolean isUnderground = worldgenRandom.nextFloat() < PROBABILITY_OF_UNDERGROUND;
         meteoritePiece$verticalPlacement = isUnderground ? MeteoritePiece.VerticalPlacement.UNDERGROUND : MeteoritePiece.VerticalPlacement.ON_LAND_SURFACE;
         meteoritePiece$properties.airPocket = isUnderground || worldgenRandom.nextFloat() < PROBABILITY_OF_AIR_POCKET;
      }

      ResourceLocation meteoriteLocation;
      if (worldgenRandom.nextFloat() < PROBABILITY_OF_GIANT_METEORITE) {
         meteoriteLocation = new ResourceLocation(STRUCTURE_LOCATION_GIANT_METEORITES[worldgenRandom.nextInt(STRUCTURE_LOCATION_GIANT_METEORITES.length)]);
      } else {
         meteoriteLocation = new ResourceLocation(STRUCTURE_LOCATION_METEORITES[worldgenRandom.nextInt(STRUCTURE_LOCATION_METEORITES.length)]);
      }

      StructureTemplate structureTemplate = pieceGeneratorSupplier$context.structureManager().getOrCreate(meteoriteLocation);
      Rotation rotation = Util.getRandom(Rotation.values(), worldgenRandom);
      Mirror mirror = worldgenRandom.nextFloat() < 0.5F ? Mirror.NONE : Mirror.FRONT_BACK;
      BlockPos templateCenterPos = new BlockPos(structureTemplate.getSize().getX() / 2, 0, structureTemplate.getSize().getZ() / 2);
      BlockPos worldPos = pieceGeneratorSupplier$context.chunkPos().getWorldPosition();
      BoundingBox boundingBox = structureTemplate.getBoundingBox(worldPos, rotation, templateCenterPos, mirror);
      BlockPos boundingBoxCenter = boundingBox.getCenter();
      int baseHeight = pieceGeneratorSupplier$context.chunkGenerator().getBaseHeight(boundingBoxCenter.getX(), boundingBoxCenter.getZ(), MeteoritePiece.getHeightMapType(meteoritePiece$verticalPlacement), pieceGeneratorSupplier$context.heightAccessor()) - 1;
      int suitableY = findSuitableY(worldgenRandom, pieceGeneratorSupplier$context.chunkGenerator(), meteoritePiece$verticalPlacement, meteoritePiece$properties.airPocket, baseHeight, boundingBox.getYSpan(), boundingBox, pieceGeneratorSupplier$context.heightAccessor());
      BlockPos targetPos = new BlockPos(worldPos.getX(), suitableY, worldPos.getZ());
      return !pieceGeneratorSupplier$context.validBiome().test(pieceGeneratorSupplier$context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(targetPos.getX()), QuartPos.fromBlock(targetPos.getY()), QuartPos.fromBlock(targetPos.getZ()))) ? Optional.empty() : Optional.of((spb, mcc) -> {
         if (meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.MOUNTAIN || meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.OCEAN || meteoriteConfiguration.meteoriteType == MeteoriteFeature.Type.STANDARD) {
            meteoritePiece$properties.cold = isCold(targetPos, pieceGeneratorSupplier$context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(targetPos.getX()), QuartPos.fromBlock(targetPos.getY()), QuartPos.fromBlock(targetPos.getZ())));
         }

         spb.addPiece(new MeteoritePiece(mcc.structureManager(), targetPos, meteoritePiece$verticalPlacement, meteoritePiece$properties, meteoriteLocation, structureTemplate, rotation, mirror, templateCenterPos));
      });
   }

   private static boolean isCold(BlockPos blockPos, Biome biome) {
      return biome.coldEnoughToSnow(blockPos);
   }

   private static int findSuitableY(Random random, ChunkGenerator chunkGenerator, MeteoritePiece.VerticalPlacement meteoritePiece$verticalPlacement, boolean hasAirPocket, int baseHeight, int ySpan, BoundingBox boundingBox, LevelHeightAccessor levelHeightProcessor) {
      int minSuitableY = levelHeightProcessor.getMinBuildHeight() + MIN_Y_INDEX;
      int startY;
      if (meteoritePiece$verticalPlacement == MeteoritePiece.VerticalPlacement.IN_END) {
         if (hasAirPocket) {
            startY = Mth.randomBetweenInclusive(random, 32, 100);
         } else if (random.nextFloat() < 0.5F) {
            startY = Mth.randomBetweenInclusive(random, 27, 29);
         } else {
            startY = Mth.randomBetweenInclusive(random, 29, 100);
         }
      } else if (meteoritePiece$verticalPlacement == MeteoritePiece.VerticalPlacement.IN_MOUNTAIN) {
         int k = baseHeight - ySpan;
         startY = getRandomWithinInterval(random, 70, k);
      } else if (meteoritePiece$verticalPlacement == MeteoritePiece.VerticalPlacement.UNDERGROUND) {
         int j1 = baseHeight - ySpan;
         startY = getRandomWithinInterval(random, minSuitableY, j1);
      } else if (meteoritePiece$verticalPlacement == MeteoritePiece.VerticalPlacement.PARTLY_BURIED) {
         startY = baseHeight - ySpan + Mth.randomBetweenInclusive(random, 2, 8);
      } else {
         startY = baseHeight;
      }

      List<BlockPos> boundPositions = ImmutableList.of(new BlockPos(boundingBox.minX(), 0, boundingBox.minZ()), new BlockPos(boundingBox.maxX(), 0, boundingBox.minZ()), new BlockPos(boundingBox.minX(), 0, boundingBox.maxZ()), new BlockPos(boundingBox.maxX(), 0, boundingBox.maxZ()));
      List<NoiseColumn> boundNoiseColumns = boundPositions.stream().map((blockPos) -> chunkGenerator.getBaseColumn(blockPos.getX(), blockPos.getZ(), levelHeightProcessor)).collect(Collectors.toList());
      Heightmap.Types heightmap$types = meteoritePiece$verticalPlacement == MeteoritePiece.VerticalPlacement.ON_OCEAN_FLOOR ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;

      int suitableY;
      for(suitableY = startY; suitableY > minSuitableY; --suitableY) {
         int i1 = 0;

         for(NoiseColumn noisecolumn : boundNoiseColumns) {
            BlockState blockstate = noisecolumn.getBlock(suitableY);
            if (heightmap$types.isOpaque().test(blockstate)) {
               ++i1;
               if (i1 == 3) {
                  return suitableY;
               }
            }
         }
      }

      return suitableY;
   }

   private static int getRandomWithinInterval(Random random, int minInclusive, int maxInclusive) {
      return minInclusive < maxInclusive ? Mth.randomBetweenInclusive(random, minInclusive, maxInclusive) : maxInclusive;
   }

   public static enum Type implements StringRepresentable {
      STANDARD("standard"),
      DESERT("desert"),
      JUNGLE("jungle"),
      SWAMP("swamp"),
      MOUNTAIN("mountain"),
      OCEAN("ocean"),
      END("end");

      public static final Codec<MeteoriteFeature.Type> CODEC =
              StringRepresentable
                      .fromEnum(MeteoriteFeature.Type::values, MeteoriteFeature.Type::byName);
      private static final Map<String, MeteoriteFeature.Type> BY_NAME =
              Arrays.stream(values())
                      .collect(Collectors.toMap(MeteoriteFeature.Type::getName, (type) -> type));
      private final String name;

      private Type(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public static MeteoriteFeature.Type byName(String name) {
         return BY_NAME.get(name);
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}
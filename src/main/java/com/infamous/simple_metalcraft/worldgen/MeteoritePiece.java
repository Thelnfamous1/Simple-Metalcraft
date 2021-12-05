package com.infamous.simple_metalcraft.worldgen;

import com.google.common.collect.Lists;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MeteoritePiece extends TemplateStructurePiece {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final float PROBABILITY_OF_RAW_MET_IRON_GONE = 0.3F;
   private static final float PROBABILITY_OF_PERIDOT_INSTEAD_OF_MET_IRON_ORE = 0.2F;
   private static final float PROBABILITY_OF_DIAMOND_INSTEAD_OF_MET_IRON_ORE = 0.02F;
   private static final float DEFAULT_MOSSINESS = 0.2F;
   private final MeteoritePiece.VerticalPlacement verticalPlacement;
   private final MeteoritePiece.Properties properties;

   public MeteoritePiece(StructureManager structureManager, BlockPos blockPos, MeteoritePiece.VerticalPlacement verticalPlacement, MeteoritePiece.Properties properties, ResourceLocation resourceLocation, StructureTemplate structureTemplate, Rotation rotation, Mirror mirror, BlockPos blockPos1) {
      super(StructureRegistration.METEORITE, 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(mirror, rotation, verticalPlacement, blockPos1, properties), blockPos);
      this.verticalPlacement = verticalPlacement;
      this.properties = properties;
   }

   public MeteoritePiece(StructureManager structureManager, CompoundTag compoundTag) {
      super(StructureRegistration.METEORITE, compoundTag, structureManager, (rl) -> makeSettings(structureManager, compoundTag, rl));
      this.verticalPlacement = MeteoritePiece.VerticalPlacement.byName(compoundTag.getString("VerticalPlacement"));
      this.properties = MeteoritePiece.Properties.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compoundTag.get("Properties"))).getOrThrow(true, LOGGER::error);
   }

   @Override
   protected void addAdditionalSaveData(StructurePieceSerializationContext serializationContext, CompoundTag compoundTag) {
      super.addAdditionalSaveData(serializationContext, compoundTag);
      compoundTag.putString("Rotation", this.placeSettings.getRotation().name());
      compoundTag.putString("Mirror", this.placeSettings.getMirror().name());
      compoundTag.putString("VerticalPlacement", this.verticalPlacement.getName());
      MeteoritePiece.Properties.CODEC.encodeStart(NbtOps.INSTANCE, this.properties).resultOrPartial(LOGGER::error).ifPresent((t) -> {
         compoundTag.put("Properties", t);
      });
   }

   private static StructurePlaceSettings makeSettings(StructureManager structureManager, CompoundTag compoundTag, ResourceLocation resourceLocation) {
      StructureTemplate structuretemplate = structureManager.getOrCreate(resourceLocation);
      BlockPos templateCenter = new BlockPos(structuretemplate.getSize().getX() / 2, 0, structuretemplate.getSize().getZ() / 2);
      return makeSettings(
              Mirror.valueOf(compoundTag.getString("Mirror")),
              Rotation.valueOf(compoundTag.getString("Rotation")),
              MeteoritePiece.VerticalPlacement.byName(compoundTag.getString("VerticalPlacement")),
              templateCenter,
              MeteoritePiece.Properties.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compoundTag.get("Properties"))).getOrThrow(true, LOGGER::error));
   }

   private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation, MeteoritePiece.VerticalPlacement verticalPlacement, BlockPos blockPos, MeteoritePiece.Properties properties) {
      BlockIgnoreProcessor blockignoreprocessor = properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
      List<ProcessorRule> processorRules = Lists.newArrayList();
      processorRules.add(getBlockReplaceRule(SMBlocks.RAW_METEORIC_IRON_BLOCK.get(), PROBABILITY_OF_RAW_MET_IRON_GONE, Blocks.AIR));
      processorRules.add(getBlockReplaceRule(SMBlocks.METEORIC_IRON_ORE.get(), PROBABILITY_OF_PERIDOT_INSTEAD_OF_MET_IRON_ORE, Blocks.EMERALD_ORE));
      if (properties.diamond) {
         processorRules.add(getBlockReplaceRule(SMBlocks.METEORIC_IRON_ORE.get(), PROBABILITY_OF_DIAMOND_INSTEAD_OF_MET_IRON_ORE, Blocks.DIAMOND_ORE));
      }

      StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings())
              .setRotation(rotation)
              .setMirror(mirror)
              .setRotationPivot(blockPos)
              .addProcessor(blockignoreprocessor)
              .addProcessor(new RuleProcessor(processorRules))
              .addProcessor(new BlockAgeProcessor(properties.mossiness))
              .addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE.getName()));

      return structureplacesettings;
   }

   @Override
   public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager featureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
      BoundingBox boundingbox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
      if (boundingBox.isInside(boundingbox.getCenter())) {
         boundingBox.encapsulate(boundingbox);
         super.postProcess(worldGenLevel, featureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
         this.spreadSuevite(random, worldGenLevel);
         this.addSueviteDripColumnsBelowMeteorite(random, worldGenLevel);
         if (this.properties.vines || this.properties.overgrown) {
            BlockPos.betweenClosedStream(this.getBoundingBox()).forEach((bp) -> {
               if (this.properties.vines) {
                  this.maybeAddVines(random, worldGenLevel, bp);
               }

               if (this.properties.overgrown) {
                  this.maybeAddLeavesAbove(random, worldGenLevel, bp);
               }

            });
         }

      }
   }

   @Override
   protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {
   }

   private void maybeAddVines(Random random, LevelAccessor levelAccessor, BlockPos blockPos) {
      BlockState blockstate = levelAccessor.getBlockState(blockPos);
      if (!blockstate.isAir() && !blockstate.is(Blocks.VINE)) {
         Direction direction = getRandomHorizontalDirection(random);
         BlockPos relativeBlockPos = blockPos.relative(direction);
         BlockState relativeBlockState = levelAccessor.getBlockState(relativeBlockPos);
         if (relativeBlockState.isAir()) {
            if (Block.isFaceFull(blockstate.getCollisionShape(levelAccessor, blockPos), direction)) {
               BooleanProperty booleanproperty = VineBlock.getPropertyForFace(direction.getOpposite());
               levelAccessor.setBlock(relativeBlockPos, Blocks.VINE.defaultBlockState().setValue(booleanproperty, Boolean.valueOf(true)), 3);
            }
         }
      }
   }

   private void maybeAddLeavesAbove(Random random, LevelAccessor levelAccessor, BlockPos blockPos) {
      if (random.nextFloat() < 0.5F && levelAccessor.getBlockState(blockPos).is(SMBlocks.SUEVITE.get()) && levelAccessor.getBlockState(blockPos.above()).isAir()) {
         levelAccessor.setBlock(blockPos.above(), Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.valueOf(true)), 3);
      }

   }

   private void addSueviteDripColumnsBelowMeteorite(Random random, LevelAccessor levelAccessor) {
      for(int i = this.boundingBox.minX() + 1; i < this.boundingBox.maxX(); ++i) {
         for(int j = this.boundingBox.minZ() + 1; j < this.boundingBox.maxZ(); ++j) {
            BlockPos blockpos = new BlockPos(i, this.boundingBox.minY(), j);
            if (levelAccessor.getBlockState(blockpos).is(SMBlocks.SUEVITE.get())) {
               this.addSueviteDripColumn(random, levelAccessor, blockpos.below());
            }
         }
      }

   }

   private void addSueviteDripColumn(Random random, LevelAccessor levelAccessor, BlockPos blockPos) {
      BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();
      this.placeSuevite(levelAccessor, blockpos$mutableblockpos);
      int i = 8;

      while(i > 0 && random.nextFloat() < 0.5F) {
         blockpos$mutableblockpos.move(Direction.DOWN);
         --i;
         this.placeSuevite(levelAccessor, blockpos$mutableblockpos);
      }

   }

   private void spreadSuevite(Random random, LevelAccessor levelAccessor) {
      boolean onGround = this.verticalPlacement == MeteoritePiece.VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == MeteoritePiece.VerticalPlacement.ON_OCEAN_FLOOR;
      BlockPos center = this.boundingBox.getCenter();
      int centerX = center.getX();
      int centerZ = center.getZ();
      float[] afloat = new float[]{1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 0.7F, 0.6F, 0.4F, 0.2F};
      int offset = afloat.length;
      int l = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
      int i1 = random.nextInt(Math.max(1, 8 - l / 2));
      int j1 = 3;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = BlockPos.ZERO.mutable();

      for(int currX = centerX - offset; currX <= centerX + offset; ++currX) {
         for(int currZ = centerZ - offset; currZ <= centerZ + offset; ++currZ) {
            int i2 = Math.abs(currX - centerX) + Math.abs(currZ - centerZ);
            int j2 = Math.max(0, i2 + i1);
            if (j2 < offset) {
               float f = afloat[j2];
               if (random.nextDouble() < (double)f) {
                  int surfaceY = getSurfaceY(levelAccessor, currX, currZ, this.verticalPlacement);
                  int adjustedSurfaceY = onGround ? surfaceY : Math.min(this.boundingBox.minY(), surfaceY);
                  blockpos$mutableblockpos.set(currX, adjustedSurfaceY, currZ);
                  if (Math.abs(adjustedSurfaceY - this.boundingBox.minY()) <= 3 && this.canBlockBeReplacedBySuevite(levelAccessor, blockpos$mutableblockpos)) {
                     this.placeSuevite(levelAccessor, blockpos$mutableblockpos);
                     if (this.properties.overgrown) {
                        this.maybeAddLeavesAbove(random, levelAccessor, blockpos$mutableblockpos);
                     }

                     this.addSueviteDripColumn(random, levelAccessor, blockpos$mutableblockpos.below());
                  }
               }
            }
         }
      }

   }

   private boolean canBlockBeReplacedBySuevite(LevelAccessor levelAccessor, BlockPos blockPos) {
      BlockState blockstate = levelAccessor.getBlockState(blockPos);
      return !blockstate.is(Blocks.AIR)
              && !blockstate.is(Blocks.OBSIDIAN)
              && !blockstate.is(BlockTags.FEATURES_CANNOT_REPLACE)
              && !blockstate.is(Blocks.LAVA);
   }

   private void placeSuevite(LevelAccessor levelAccessor, BlockPos blockPos) {
      levelAccessor.setBlock(blockPos, SMBlocks.SUEVITE.get().defaultBlockState(), 3);
   }

   private static int getSurfaceY(LevelAccessor levelAccessor, int p_72671_, int p_72672_, MeteoritePiece.VerticalPlacement verticalPlacement) {
      return levelAccessor.getHeight(getHeightMapType(verticalPlacement), p_72671_, p_72672_) - 1;
   }

   public static Heightmap.Types getHeightMapType(MeteoritePiece.VerticalPlacement verticalPlacement) {
      return verticalPlacement == MeteoritePiece.VerticalPlacement.ON_OCEAN_FLOOR ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
   }

   private static ProcessorRule getBlockReplaceRule(Block inputBlock, float probability, Block outputBlock) {
      return new ProcessorRule(new RandomBlockMatchTest(inputBlock, probability), AlwaysTrueTest.INSTANCE, outputBlock.defaultBlockState());
   }

   private static ProcessorRule getBlockReplaceRule(Block inputBlock, Block outputBlock) {
      return new ProcessorRule(new BlockMatchTest(inputBlock), AlwaysTrueTest.INSTANCE, outputBlock.defaultBlockState());
   }

   public static class Properties {
      public static final Codec<MeteoritePiece.Properties> CODEC = RecordCodecBuilder
              .create((propertiesInstance) -> propertiesInstance
                      .group(Codec.BOOL.fieldOf("diamond").forGetter((p) -> p.diamond),
                              Codec.FLOAT.fieldOf("mossiness").forGetter((mp$p) -> mp$p.mossiness),
                              Codec.BOOL.fieldOf("air_pocket").forGetter((mp$p) -> mp$p.airPocket),
                              Codec.BOOL.fieldOf("overgrown").forGetter((mp$p) -> mp$p.overgrown),
                              Codec.BOOL.fieldOf("vines").forGetter((mp$p) -> mp$p.vines))
                      .apply(propertiesInstance, Properties::new));
      public boolean diamond;
      public float mossiness = DEFAULT_MOSSINESS;
      public boolean airPocket;
      public boolean overgrown;
      public boolean vines;

      public Properties() {
      }

      public Properties(boolean diamond, float mossiness, boolean airPocket, boolean overgrown, boolean vines) {
         this.diamond = diamond;
         this.mossiness = mossiness;
         this.airPocket = airPocket;
         this.overgrown = overgrown;
         this.vines = vines;
      }
   }

   public enum VerticalPlacement {
      ON_LAND_SURFACE("on_land_surface"),
      PARTLY_BURIED("partly_buried"),
      ON_OCEAN_FLOOR("on_ocean_floor"),
      IN_MOUNTAIN("in_mountain"),
      UNDERGROUND("underground"),
      IN_END("in_end");

      private static final Map<String, MeteoritePiece.VerticalPlacement> BY_NAME = Arrays.stream(values())
              .collect(Collectors.toMap(MeteoritePiece.VerticalPlacement::getName, (mp$vp) -> mp$vp));
      private final String name;

      VerticalPlacement(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public static MeteoritePiece.VerticalPlacement byName(String name) {
         return BY_NAME.get(name);
      }
   }
}
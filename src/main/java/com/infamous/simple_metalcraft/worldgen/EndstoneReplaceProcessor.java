package com.infamous.simple_metalcraft.worldgen;

import com.google.common.collect.Maps;
import com.infamous.simple_metalcraft.SMModEvents;
import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class EndstoneReplaceProcessor extends StructureProcessor {
   public static final Codec<EndstoneReplaceProcessor> CODEC;
   public static final EndstoneReplaceProcessor INSTANCE = new EndstoneReplaceProcessor();
   private final Map<Block, Block> replacements = Util.make(Maps.newHashMap(), (m) -> {
      m.put(Blocks.COBBLESTONE, Blocks.END_STONE);
      m.put(Blocks.MOSSY_COBBLESTONE, Blocks.END_STONE);
      m.put(Blocks.STONE, Blocks.END_STONE);
      m.put(Blocks.STONE_BRICKS, Blocks.END_STONE_BRICKS);
      m.put(Blocks.MOSSY_STONE_BRICKS, Blocks.END_STONE_BRICKS);
      m.put(Blocks.COBBLESTONE_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
      m.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
      m.put(Blocks.STONE_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
      m.put(Blocks.STONE_BRICK_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
      m.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
      m.put(Blocks.COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.SMOOTH_STONE_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.STONE_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.STONE_BRICK_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.END_STONE_BRICK_SLAB);
      m.put(Blocks.STONE_BRICK_WALL, Blocks.END_STONE_BRICK_WALL);
      m.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.END_STONE_BRICK_WALL);
      m.put(Blocks.COBBLESTONE_WALL, Blocks.END_STONE_BRICK_WALL);
      m.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.END_STONE_BRICK_WALL);
      m.put(Blocks.CHISELED_STONE_BRICKS, Blocks.END_STONE);
      m.put(Blocks.CRACKED_STONE_BRICKS, Blocks.END_STONE_BRICKS);
      m.put(Blocks.IRON_BARS, Blocks.CHAIN);
   });

   private EndstoneReplaceProcessor() {
   }

   @Override
   public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
      Block newBlock = this.replacements.get(structureBlockInfo1.state.getBlock());
      if (newBlock == null) {
         return structureBlockInfo1;
      } else {
         BlockState oldBlockState = structureBlockInfo1.state;
         BlockState newBlockState = newBlock.defaultBlockState();
         if (oldBlockState.hasProperty(StairBlock.FACING)) {
            newBlockState = newBlockState.setValue(StairBlock.FACING, oldBlockState.getValue(StairBlock.FACING));
         }

         if (oldBlockState.hasProperty(StairBlock.HALF)) {
            newBlockState = newBlockState.setValue(StairBlock.HALF, oldBlockState.getValue(StairBlock.HALF));
         }

         if (oldBlockState.hasProperty(SlabBlock.TYPE)) {
            newBlockState = newBlockState.setValue(SlabBlock.TYPE, oldBlockState.getValue(SlabBlock.TYPE));
         }

         return new StructureTemplate.StructureBlockInfo(structureBlockInfo1.pos, newBlockState, structureBlockInfo1.nbt);
      }
   }

   protected StructureProcessorType<?> getType() {
      return SMModEvents.ENDSTONE_REPLACE;
   }

   static {
      CODEC = Codec.unit(() -> INSTANCE);
   }
}
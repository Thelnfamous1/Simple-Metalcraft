package com.infamous.simple_metalcraft.crafting.batch.cementation;

import com.infamous.simple_metalcraft.crafting.batch.BatchFurnaceBlockEntity;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class CementationFurnaceBlock extends AbstractFurnaceBlock {

   public CementationFurnaceBlock(Properties properties) {
      super(properties);
   }

   @Override
   public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
      return new CementationFurnaceBlockEntity(blockPos, blockState);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
      return createCementationFurnaceTicker(level, blockEntityType, SMBlockEntityTypes.CEMENTATION_FURNACE.get());
   }

   @Nullable
   protected static <T extends BlockEntity> BlockEntityTicker<T> createCementationFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<CementationFurnaceBlockEntity> furnaceEntityType) {
      return level.isClientSide ? null : createTickerHelper(blockEntityType, furnaceEntityType, BatchFurnaceBlockEntity::batchServerTick);
   }

   @Override
   protected void openContainer(Level level, BlockPos blockPos, Player player) {
      BlockEntity blockentity = level.getBlockEntity(blockPos);
      if (blockentity instanceof CementationFurnaceBlockEntity) {
         player.openMenu((MenuProvider)blockentity);
         player.awardStat(Stats.INTERACT_WITH_FURNACE);
      }

   }

   @Override
   public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
      if (blockState.getValue(LIT)) {
         double xPos = (double)blockPos.getX() + 0.5D;
         double yPos = (double)blockPos.getY();
         double zPos = (double)blockPos.getZ() + 0.5D;
         if (random.nextDouble() < 0.1D) {
            level.playLocalSound(xPos, yPos, zPos, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
         }

         level.addParticle(ParticleTypes.SMOKE, xPos, yPos + 1.1D, zPos, 0.0D, 0.0D, 0.0D);
      }
   }
}
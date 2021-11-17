package com.infamous.simple_metalcraft.crafting.blooming;

import java.util.Random;
import javax.annotation.Nullable;

import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BloomeryBlock extends AbstractFurnaceBlock {
   public BloomeryBlock(BlockBehaviour.Properties properties) {
      super(properties);
   }

   public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
      return new BloomeryBlockEntity(blockPos, blockState);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
      return createBloomeryTicker(level, blockEntityType, SMBlockEntityTypes.BLOOMERY.get());
   }

   @Nullable
   protected static <T extends BlockEntity> BlockEntityTicker<T> createBloomeryTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<BloomeryBlockEntity> furnaceEntityType) {
      return level.isClientSide ? null : createTickerHelper(blockEntityType, furnaceEntityType, BloomeryBlockEntity::bloomeryServerTick);
   }

   protected void openContainer(Level level, BlockPos blockPos, Player player) {
      BlockEntity blockentity = level.getBlockEntity(blockPos);
      if (blockentity instanceof BloomeryBlockEntity) {
         player.openMenu((MenuProvider)blockentity);
         player.awardStat(Stats.INTERACT_WITH_FURNACE);
      }

   }

   public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
      if (blockState.getValue(AbstractFurnaceBlock.LIT)) {
         double xPos = (double)blockPos.getX() + 0.5D;
         double yPos = (double)blockPos.getY();
         double zPos = (double)blockPos.getZ() + 0.5D;
         if (random.nextDouble() < 0.1D) {
            level.playLocalSound(xPos, yPos, zPos, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
         }

         Direction facing = blockState.getValue(AbstractFurnaceBlock.FACING);
         Direction.Axis facingAxis = facing.getAxis();
         double stepScale = 0.52D;
         double defaultDelta = random.nextDouble() * 0.6D - 0.3D;
         double xDelta = facingAxis == Direction.Axis.X ? (double)facing.getStepX() * stepScale : defaultDelta;
         double yDelta = random.nextDouble() * 6.0D / 16.0D;
         double zDelta = facingAxis == Direction.Axis.Z ? (double)facing.getStepZ() * stepScale : defaultDelta;
         level.addParticle(ParticleTypes.SMOKE, xPos + xDelta, yPos + yDelta, zPos + zDelta, 0.0D, 0.0D, 0.0D);
         level.addParticle(ParticleTypes.FLAME, xPos + xDelta, yPos + yDelta, zPos + zDelta, 0.0D, 0.0D, 0.0D);
      }
   }
}
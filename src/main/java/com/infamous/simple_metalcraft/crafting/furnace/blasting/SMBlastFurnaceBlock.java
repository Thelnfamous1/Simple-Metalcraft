package com.infamous.simple_metalcraft.crafting.furnace.blasting;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedFurnaceBlockEntity;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SMBlastFurnaceBlock extends BlastFurnaceBlock {

    public SMBlastFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SMBlastFurnaceBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createBlastFurnaceTicker(level, blockEntityType, SMBlockEntityTypes.BLAST_FURNACE.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createBlastFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends SMBlastFurnaceBlockEntity> specificBET) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, specificBET, AdvancedFurnaceBlockEntity::advancedServerTick);
    }

    @Override
    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof SMBlastFurnaceBlockEntity) {
            player.openMenu((MenuProvider)blockentity);
            player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
        }

    }
}

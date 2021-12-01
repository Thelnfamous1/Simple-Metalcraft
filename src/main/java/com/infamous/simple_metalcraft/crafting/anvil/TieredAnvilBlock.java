package com.infamous.simple_metalcraft.crafting.anvil;

import com.infamous.simple_metalcraft.entity.SMFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class TieredAnvilBlock extends AnvilBlock {
    protected final AnvilTier anvilTier;
    private static final Component ANVIL_CONTAINER_TITLE = new TranslatableComponent("container.repair");

    public TieredAnvilBlock(AnvilTier anvilTier, Properties properties) {
        super(properties);
        this.anvilTier = anvilTier;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (isFree(serverLevel.getBlockState(blockPos.below())) && blockPos.getY() >= serverLevel.getMinBuildHeight()) {
            FallingBlockEntity fbe = new SMFallingBlockEntity(
                    serverLevel,
                    (double)blockPos.getX() + 0.5D,
                    (double)blockPos.getY(),
                    (double)blockPos.getZ() + 0.5D,
                    serverLevel.getBlockState(blockPos)
            );
            this.falling(fbe);
            serverLevel.addFreshEntity(fbe);
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return new SimpleMenuProvider(
                (containerId, inventory, player) ->
                        new SMAnvilMenu(containerId, inventory, ContainerLevelAccess.create(level, blockPos)),
                ANVIL_CONTAINER_TITLE
        );
    }

    @Nullable
    public static BlockState tieredDamage(BlockState blockState) {
        if(blockState.getBlock() instanceof TieredAnvilBlock tieredAnvil){
            if (blockState.is(tieredAnvil.getAnvil())) {
                return tieredAnvil.getChippedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING));
            } else {
                return blockState.is(tieredAnvil.getChippedAnvil()) ? tieredAnvil.getDamagedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING)) : null;
            }
        } else{
            return AnvilBlock.damage(blockState);
        }
    }

    public AnvilTier getAnvilTier() {
        return this.anvilTier;
    }

    public Block getAnvil(){
        return this.anvilTier.getAnvil();
    }

    public Block getChippedAnvil(){
        return this.anvilTier.getChippedAnvil();
    }

    public Block getDamagedAnvil(){
        return this.anvilTier.getDamagedAnvil();
    }
}

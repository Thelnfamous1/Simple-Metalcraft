package com.infamous.simple_metalcraft.crafting.anvil;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TieredAnvilBlock extends AnvilBlock {
    protected final AnvilTier anvilTier;

    public TieredAnvilBlock(AnvilTier anvilTier, Properties properties) {
        super(properties);
        this.anvilTier = anvilTier;
    }

    @Nullable
    public static BlockState damage(BlockState blockState, TieredAnvilBlock tieredAnvil) {
        if (blockState.is(tieredAnvil.getAnvil())) {
            return tieredAnvil.getChippedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING));
        } else {
            return blockState.is(tieredAnvil.getChippedAnvil()) ? tieredAnvil.getDamagedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING)) : null;
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

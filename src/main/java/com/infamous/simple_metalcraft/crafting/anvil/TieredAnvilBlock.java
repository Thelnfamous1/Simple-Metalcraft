package com.infamous.simple_metalcraft.crafting.anvil;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TieredAnvilBlock extends AnvilBlock {
    private final AnvilTier anvilTier;

    public TieredAnvilBlock(AnvilTier anvilTier, Properties properties) {
        super(properties);
        this.anvilTier = anvilTier;
    }

    @Nullable
    public static BlockState damage(BlockState blockState, TieredAnvilBlock tieredAnvil) {
        AnvilTier anvilTier = tieredAnvil.getAnvilTier();
        if (blockState.is(anvilTier.getAnvil())) {
            return anvilTier.getChippedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING));
        } else {
            return blockState.is(anvilTier.getChippedAnvil()) ? anvilTier.getDamagedAnvil().defaultBlockState().setValue(FACING, blockState.getValue(FACING)) : null;
        }
    }

    public AnvilTier getAnvilTier() {
        return this.anvilTier;
    }
}

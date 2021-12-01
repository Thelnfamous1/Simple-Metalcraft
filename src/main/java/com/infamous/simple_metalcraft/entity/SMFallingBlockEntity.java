package com.infamous.simple_metalcraft.entity;

import com.infamous.simple_metalcraft.crafting.anvil.TieredAnvilBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class SMFallingBlockEntity extends FallingBlockEntity {

    public SMFallingBlockEntity(Level level, double x, double y, double z, BlockState blockState) {
        super(level, x, y, z, blockState);
    }

    @Override
    public boolean causeFallDamage(float fallDistanceIn, float fallDamageIn, DamageSource damageSourceIn) {
        if (this.hurtEntities) {
            int fallenDistance = Mth.ceil(fallDistanceIn - 1.0F);
            if (fallenDistance >= 0) {
                Predicate<Entity> predicate;
                DamageSource fallDamageSource;
                if (this.blockState.getBlock() instanceof Fallable fallable) {
                    predicate = fallable.getHurtsEntitySelector();
                    fallDamageSource = fallable.getFallDamageSource();
                } else {
                    predicate = EntitySelector.NO_SPECTATORS;
                    fallDamageSource = DamageSource.FALLING_BLOCK;
                }

                float fallDamage = (float) Math.min(Mth.floor((float) fallenDistance * this.fallDamagePerDistance), this.fallDamageMax);
                this.level.getEntities(this, this.getBoundingBox(), predicate).forEach((e) -> e.hurt(fallDamageSource, fallDamage));
                boolean isAnvil = this.blockState.is(BlockTags.ANVIL);
                if (isAnvil && fallDamage > 0.0F && this.random.nextFloat() < 0.05F + (float) fallenDistance * 0.05F) {
                    BlockState blockstate = TieredAnvilBlock.tieredDamage(this.blockState);
                    if (blockstate == null) {
                        this.cancelDrop = true;
                    } else {
                        this.blockState = blockstate;
                    }
                }

            }
        }
        return false;
    }
}

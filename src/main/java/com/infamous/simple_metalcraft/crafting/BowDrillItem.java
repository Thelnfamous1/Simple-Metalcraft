package com.infamous.simple_metalcraft.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BowDrillItem extends FlintAndSteelItem {

    public static final float FAILURE_CHANCE = 0.75F;

    public BowDrillItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);
        BlockPos facePos = clickedPos.relative(context.getClickedFace());

        boolean canPlaceFire = BaseFireBlock.canBePlacedAt(level, facePos, context.getHorizontalDirection());
        boolean tryIgnite = canPlaceFire || isIgnitableBlock(blockState);
        if(tryIgnite && level.getRandom().nextFloat() < FAILURE_CHANCE){
            level.playSound(player, facePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, clickedPos);
            if(player != null){
                context.getItemInHand().hurtAndBreak(1, player, (p) -> {
                    p.broadcastBreakEvent(context.getHand());
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else{
            return super.useOn(context);
        }
    }

    private static boolean isIgnitableBlock(BlockState blockstate){
        return CampfireBlock.canLight(blockstate) || CandleBlock.canLight(blockstate) || CandleCakeBlock.canLight(blockstate);
    }
}

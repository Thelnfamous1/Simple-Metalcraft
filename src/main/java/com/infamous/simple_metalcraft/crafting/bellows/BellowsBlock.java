package com.infamous.simple_metalcraft.crafting.bellows;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.mixin.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

public class BellowsBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final int DEFAULT_PRESS_DURATION = 20;
    public static final int DEFAULT_COOKING_BOOST = 20;

    public BellowsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.valueOf(false)).setValue(FACE, AttachFace.WALL));
    }

    protected int getPressDuration() {
        return DEFAULT_PRESS_DURATION;
    }

    protected int getCookingBoost(){
        return DEFAULT_COOKING_BOOST;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (blockState.getValue(POWERED)) {
            return InteractionResult.CONSUME;
        } else {
            this.press(blockState, level, blockPos);
            this.playSound(player, level, blockPos, true);
            level.gameEvent(player, GameEvent.BLOCK_PRESS, blockPos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public void press(BlockState blockState, Level level, BlockPos blockPos) {
        level.setBlock(blockPos, blockState.setValue(POWERED, Boolean.valueOf(true)), 3);
        this.updateNeighbours(blockState, level, blockPos);
        level.getBlockTicks().scheduleTick(blockPos, this, this.getPressDuration());
        this.boostConnectedFurnace(blockState, level, blockPos);
    }

    private void boostConnectedFurnace(BlockState blockState, Level level, BlockPos blockPos) {
        BlockPos connectedPos = blockPos.relative(getConnectedDirection(blockState).getOpposite());
        BlockEntity blockEntity = level.getBlockEntity(connectedPos);
        if(blockEntity != null) SimpleMetalcraft.LOGGER.info("Found a {} at {}!", blockEntity.getType(), blockPos);
        if(!level.isClientSide && blockEntity instanceof AbstractFurnaceBlockEntity furnaceBE){
            SimpleMetalcraft.LOGGER.info("Found a furnace at {}!", blockPos);
            AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor)furnaceBE;
            int cookingProgress = accessor.getCookingProgress();
            if(accessor.callIsLit() && cookingProgress > 0){
                accessor.setCookingProgress(Mth.clamp(cookingProgress + this.getCookingBoost(), 0, accessor.getCookingTotalTime() - 1));
            }
        }
    }

    protected void playSound(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos, boolean on) {
        levelAccessor.playSound(on ? player : null, blockPos, this.getSound(on), SoundSource.BLOCKS, 0.3F, on ? 0.6F : 0.5F);
    }

    protected SoundEvent getSound(boolean on) {
        return on ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos blockPos, BlockState newState, boolean b) {
        if (!b && !oldState.is(newState.getBlock())) {
            if (oldState.getValue(POWERED)) {
                this.updateNeighbours(oldState, level, blockPos);
            }

            super.onRemove(oldState, level, blockPos, newState, b);
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos p_51080_, Direction direction) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) && getConnectedDirection(blockState) == direction ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (blockState.getValue(POWERED)) {
            serverLevel.setBlock(blockPos, blockState.setValue(POWERED, Boolean.valueOf(false)), 3);
            this.updateNeighbours(blockState, serverLevel, blockPos);
            this.playSound((Player)null, serverLevel, blockPos, false);
            serverLevel.gameEvent(GameEvent.BLOCK_UNPRESS, blockPos);

        }
    }

    private void updateNeighbours(BlockState blockState, Level level, BlockPos blockPos) {
        level.updateNeighborsAt(blockPos, this);
        level.updateNeighborsAt(blockPos.relative(getConnectedDirection(blockState).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, POWERED, FACE);
    }
}

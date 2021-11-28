package com.infamous.simple_metalcraft.crafting.bellows;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
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
    public static final int DEFAULT_PRESS_DURATION = 10;
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, context.getHorizontalDirection());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction);
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

    @Override
    public boolean canSurvive(BlockState p_53186_, LevelReader p_53187_, BlockPos p_53188_) {
        return true;
    }

    public void press(BlockState blockState, Level level, BlockPos blockPos) {
        level.setBlock(blockPos, blockState.setValue(POWERED, Boolean.valueOf(true)), 3);
        this.updateNeighbours(blockState, level, blockPos);
        level.getBlockTicks().scheduleTick(blockPos, this, this.getPressDuration());
        this.tickConnectedFurnace(blockState, level, blockPos);
    }

    @SuppressWarnings("unchecked")
    private <T extends BlockEntity> void tickConnectedFurnace(BlockState blockState, Level level, BlockPos blockPos) {
        BlockPos connectedPos = getConnectedPos(blockState, blockPos);
        BlockState connectedBS = level.getBlockState(connectedPos);
        BlockEntity connectedBE = level.getBlockEntity(connectedPos);

        if (connectedBE instanceof AbstractFurnaceBlockEntity) { // TODO: To hardcode or not to hardcode?
            //SimpleMetalcraft.LOGGER.info("Connected to {}", connectedBE);
            BlockEntityTicker<T> ticker = (BlockEntityTicker<T>) connectedBS.getTicker(level, connectedBE.getType());
            if(ticker != null){
                //SimpleMetalcraft.LOGGER.info("Boost ticking {}", connectedBE);
                for(int i = 0; i < this.getCookingBoost(); i++){
                    ticker.tick(level, connectedPos, connectedBS, (T) connectedBE);
                }
            }
        }
    }

    protected void playSound(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos, boolean on) {
        levelAccessor.playSound(on ? player : null, blockPos, this.getSound(on), SoundSource.BLOCKS, 0.3F, on ? 0.6F : 0.5F);
    }

    protected SoundEvent getSound(boolean on) {
        return on ? SoundEvents.PISTON_CONTRACT : SoundEvents.PISTON_EXTEND;
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
        level.updateNeighborsAt(this.getConnectedPos(blockState, blockPos), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, POWERED, FACE);
    }

    private BlockPos getConnectedPos(BlockState blockState, BlockPos blockPos) {
        return blockPos.relative(getConnectedDirection(blockState).getOpposite());
    }
}

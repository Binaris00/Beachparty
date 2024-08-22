package satisfy.beachparty.block;

import com.mojang.serialization.MapCodec;
import de.cristelknight.doapi.common.util.GeneralUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import satisfy.beachparty.util.HammockPart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HammockBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<HammockBlock> CODEC = simpleCodec(HammockBlock::new);
    public static final EnumProperty<HammockPart> PART;
    public static final BooleanProperty OCCUPIED;


    private static final Supplier<VoxelShape> leftShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(-0.1875, 0.62, 0.125, 0.07499999999999996, 0.6825, 1));
        shape = Shapes.or(shape, Shapes.box(0.6875, 0, 0.4375, 0.8125, 1, 0.5625));
        shape = Shapes.or(shape, Shapes.box(0.6875, 1, 0.4375, 0.8125, 1.3125, 0.5625));

        return shape;
    };

    public static final Map<Direction, VoxelShape> LEFT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, leftShapeSupplier.get()));
        }
    });

    private static final Supplier<VoxelShape> middleShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0.1875, 0.62, 0.125, 0.8125, 0.6825, 1));

        return shape;
    };

    public static final Map<Direction, VoxelShape> MIDDLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, middleShapeSupplier.get()));
        }
    });

    private static final Supplier<VoxelShape> rightShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.or(shape, Shapes.box(0.925, 0.62, 0.125, 1.1875, 0.6825, 1));
        shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.4375, 0.3125, 1, 0.5625));
        shape = Shapes.or(shape, Shapes.box(0.1875, 1, 0.4375, 0.3125, 1.3125, 0.5625));

        return shape;
    };

    public static final Map<Direction, VoxelShape> RIGHT_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, rightShapeSupplier.get()));
        }
    });

    public HammockBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(PART, HammockPart.MIDDLE).setValue(OCCUPIED, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return state.getValue(PART) == HammockPart.LEFT ? LEFT_SHAPE.get(direction) : state.getValue(PART) == HammockPart.RIGHT ? RIGHT_SHAPE.get(direction) : MIDDLE_SHAPE.get(direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        BlockPos middle = ctx.getClickedPos();
        BlockPos left = middle.relative(direction.getCounterClockWise(Direction.Axis.Y));
        BlockPos right = middle.relative(direction.getClockWise(Direction.Axis.Y));
        Level world = ctx.getLevel();
        return world.getBlockState(left).canBeReplaced(ctx) && world.getWorldBorder().isWithinBounds(left) && world.getBlockState(right).canBeReplaced(ctx) && world.getWorldBorder().isWithinBounds(right)  ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (!world.isClientSide) {
            BlockPos left = pos.relative(state.getValue(FACING).getClockWise(Direction.Axis.Y));
            BlockPos right = pos.relative(state.getValue(FACING).getCounterClockWise(Direction.Axis.Y));
            world.setBlock(left, state.setValue(PART, HammockPart.LEFT), 3);
            world.setBlock(right, state.setValue(PART, HammockPart.RIGHT), 3);
            world.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        HammockPart part = state.getValue(PART);
        Direction direction = state.getValue(FACING);
        switch (part) {
            case MIDDLE -> {
                BlockPos left = pos.relative(direction.getClockWise(Direction.Axis.Y));
                BlockPos right = pos.relative(direction.getCounterClockWise(Direction.Axis.Y));
                if (world.getBlockState(left).getBlock() == this) world.setBlock(left, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                if (world.getBlockState(right).getBlock() == this) world.setBlock(right, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            }
            case LEFT -> {
                BlockPos middle = pos.relative(direction.getCounterClockWise(Direction.Axis.Y));
                BlockPos right = pos.relative(direction.getCounterClockWise(Direction.Axis.Y)).relative(direction.getCounterClockWise(Direction.Axis.Y));
                if (world.getBlockState(middle).getBlock() == this) world.setBlock(middle, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                if (world.getBlockState(right).getBlock() == this) world.setBlock(right, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            }
            case RIGHT -> {
                BlockPos middle = pos.relative(direction.getClockWise(Direction.Axis.Y));
                BlockPos left = pos.relative(direction.getClockWise(Direction.Axis.Y)).relative(direction.getClockWise(Direction.Axis.Y));
                if (world.getBlockState(middle).getBlock() == this) world.setBlock(middle, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                if (world.getBlockState(left).getBlock() == this) world.setBlock(left, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }


    private boolean isHammockWhole(BlockState state, Level world, BlockPos pos) {
        HammockPart part = state.getValue(PART);
        Direction direction = state.getValue(FACING);
        switch (part) {
            case MIDDLE -> {
                BlockPos left = pos.relative(direction.getClockWise(Direction.Axis.Y));
                BlockPos right = pos.relative(direction.getCounterClockWise(Direction.Axis.Y));
                return  (world.getBlockState(left).getBlock() == this) && (world.getBlockState(right).getBlock() == this);
            }
            case LEFT -> {
                BlockPos middle = pos.relative(direction.getCounterClockWise(Direction.Axis.Y));
                BlockPos right = pos.relative(direction.getCounterClockWise(Direction.Axis.Y)).relative(direction.getCounterClockWise(Direction.Axis.Y));
                return  (world.getBlockState(middle).getBlock() == this) && (world.getBlockState(right).getBlock() == this);
            }
            case RIGHT -> {
                BlockPos middle = pos.relative(direction.getClockWise(Direction.Axis.Y));
                BlockPos left = pos.relative(direction.getClockWise(Direction.Axis.Y)).relative(direction.getClockWise(Direction.Axis.Y));
                return  (world.getBlockState(middle).getBlock() == this) && (world.getBlockState(left).getBlock() == this);}
        }
        return false;
    }

    public static boolean isBedWorking(Level world) {
        return world.dimensionType().bedWorks();
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, Direction.UP);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    static {
        PART = EnumProperty.create("part", HammockPart.class);
        OCCUPIED = BlockStateProperties.OCCUPIED;
    }

}

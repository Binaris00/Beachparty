package satisfy.beachparty.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.registry.ObjectRegistry;

@SuppressWarnings("deprecation")
public class SandCastleBlock extends Block {
    private static final VoxelShape BASE_SHAPE = Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0), Block.box(2.0, 1.0, 2.0, 14.0, 6.0, 14.0));
    public static final BooleanProperty TALL_TOWER;
    public static final VoxelShape TALL_TOWER_SHAPE = Block.box(11.0, 0.0, 11.0, 15.0, 15.0, 15.0);
    public static final BooleanProperty RIGHT_TOWER;
    public static final VoxelShape RIGHT_TOWER_SHAPE = Block.box(1.0, 1.0, 11.0, 5.0, 12.0, 15.0);
    public static final BooleanProperty TOP_TOWER;
    public static final VoxelShape TOP_TOWER_SHAPE = Block.box(5.0, 6.0, 5.0, 11.0, 9.0, 11.0);
    public static final BooleanProperty LEFT_TOWER;
    public static final VoxelShape LEFT_TOWER_SHAPE = Block.box(11.0, 1.0, 1.0, 15.0, 12.0, 5.0);


    public SandCastleBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(TALL_TOWER, false).setValue(RIGHT_TOWER, false).setValue(TOP_TOWER, false).setValue(LEFT_TOWER, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.or(BASE_SHAPE);
        if (state.getValue(TALL_TOWER)) {
            shape = Shapes.or(shape, TALL_TOWER_SHAPE);
        }
        if (state.getValue(RIGHT_TOWER)) {
            shape = Shapes.or(shape, RIGHT_TOWER_SHAPE);
        }
        if (state.getValue(LEFT_TOWER)) {
            shape = Shapes.or(shape, LEFT_TOWER_SHAPE);
        }
        if (state.getValue(TOP_TOWER)) {
            shape = Shapes.or(shape, TOP_TOWER_SHAPE);
        }
        return shape;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        InteractionHand hand = blockHitResult.getDirection() == Direction.UP ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.getItem() == ObjectRegistry.SAND_BUCKET.get() && !hasAllTowers(state)) {
            BooleanProperty tower = getTowerHitPos(blockHitResult);
            if (!state.getValue(tower)) {
                world.setBlockAndUpdate(pos, state.setValue(tower, true));
                exchangeStack(handStack, player, new ItemStack(ObjectRegistry.EMPTY_SAND_BUCKET.get()));
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        else if (handStack.getItem() == ObjectRegistry.EMPTY_SAND_BUCKET.get()) {
            if (hasNoTowers(state)) {
                world.destroyBlock(pos, false);
                exchangeStack(handStack, player, new ItemStack(ObjectRegistry.SAND_BUCKET.get()));
                return InteractionResult.sidedSuccess(world.isClientSide);
            } else if (!hasNoTowers(state)) {
                BooleanProperty tower = getTowerHitPos(blockHitResult);
                if (state.getValue(tower)) {
                    world.setBlockAndUpdate(pos, state.setValue(tower, false));
                    exchangeStack(handStack, player, new ItemStack(ObjectRegistry.SAND_BUCKET.get()));
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }

            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        world.setBlock(pos, ObjectRegistry.SAND_PILE.get().defaultBlockState(), 3);
    }

    private void exchangeStack(ItemStack handStack, Player player, ItemStack possibleReturnStack) {
        ItemStack returnStack = !player.getAbilities().instabuild ? possibleReturnStack : handStack;
        if (player.isCreative()) {
            if (!player.getInventory().contains(returnStack)) {
                player.getInventory().add(returnStack);
            }
            return;
        }
        Inventory inventory = player.getInventory();
        int slot = inventory.findSlotMatchingItem(handStack);
        handStack.shrink(1);
        if(player.getInventory().getItem(slot).isEmpty()) {
            if (!inventory.add(slot, returnStack)) {
                player.drop(returnStack, false);
            }
        } else {
            if (!inventory.add(returnStack)) {
                player.drop(returnStack, false);
            }
        }
    }

    private BooleanProperty getTowerHitPos(BlockHitResult hitResult) {
        double x = hitResult.getLocation().x();
        double z = hitResult.getLocation().z();

        double relX = x - hitResult.getBlockPos().getX();
        double relZ = z - hitResult.getBlockPos().getZ();

        if (relX >= 0.5 && relZ >= 0.5) {
            return TALL_TOWER;
        } else if (relX < 0.5 && relZ >= 0.5) {
            return RIGHT_TOWER;
        } else if (relX >= 0.5 && relZ < 0.5) {
            return LEFT_TOWER;
        } else {
            return TOP_TOWER;
        }
    }

    private boolean hasAllTowers(BlockState state) {
        return state.getValue(TALL_TOWER) && state.getValue(RIGHT_TOWER) && state.getValue(TOP_TOWER) && state.getValue(LEFT_TOWER);
    }

    private boolean hasNoTowers(BlockState state) {
        return !state.getValue(TALL_TOWER) && !state.getValue(RIGHT_TOWER) && !state.getValue(TOP_TOWER) && !state.getValue(LEFT_TOWER);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
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
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(ObjectRegistry.SAND_BUCKET.get());
    }

    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TALL_TOWER, RIGHT_TOWER, TOP_TOWER, LEFT_TOWER);
    }

    static {
        TALL_TOWER = BooleanProperty.create("tall");
        RIGHT_TOWER = BooleanProperty.create("right");
        TOP_TOWER = BooleanProperty.create("top");
        LEFT_TOWER = BooleanProperty.create("left");
    }
}
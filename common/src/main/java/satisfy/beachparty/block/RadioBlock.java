package satisfy.beachparty.block;

import de.cristelknight.doapi.common.util.GeneralUtil;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.networking.packet.TurnRadioS2CPacket;
import satisfy.beachparty.registry.SoundEventRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class RadioBlock extends Block {
    public static final BooleanProperty ON;
    public static final IntegerProperty CHANNEL;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty SEARCHING;
    public static final int CHANNELS = SoundEventRegistry.RADIO_SOUNDS.size();
    public static final int DELAY = 2 * 20;

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> Shapes.box(0.125, 0, 0.3125, 0.875, 0.5, 0.6875);

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public RadioBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ON, false).setValue(CHANNEL, 0).setValue(SEARCHING, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int channel = RandomSource.create().nextIntBetweenInclusive(0, CHANNELS - 1);
        Direction facing = Direction.NORTH;
        if (context.getHorizontalDirection().getAxis() != Direction.Axis.Y) {
            facing = context.getHorizontalDirection().getOpposite();
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(CHANNEL, channel);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        InteractionHand hand = blockHitResult.getDirection() == Direction.UP ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        if (state.getValue(SEARCHING)) {
            return InteractionResult.CONSUME;
        }
        if (hand == InteractionHand.MAIN_HAND) {
            boolean newState = !state.getValue(ON);
            if (newState) {
                turnON(state, world, pos, player);
                spawnParticles(world, pos);
            } else {
                turnOFF(state, world, pos, player);
            }
            if (!world.isClientSide) {
                if (newState) {
                    for (int i = 0; i < 5; i++) {
                        double x = pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5;
                        double y = pos.getY() + 0.5 + world.random.nextDouble() * 0.5;
                        double z = pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5;
                        world.addParticle(ParticleTypes.NOTE, x, y, z, 0, 0, 0);
                        world.addParticle(ParticleTypes.NOTE, x, y, z, 0.1, 0, 0);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, blockHitResult);
    }

    private void turnON(BlockState state, Level world, BlockPos pos, Player player) {
        world.playSound(player, pos, SoundEventRegistry.RADIO_CLICK.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        world.playSound(player, pos, SoundEventRegistry.RADIO_TUNE.get(), SoundSource.RECORDS, 0.8f, 1.0f);
        if (!world.isClientSide) {
            pressButton(state, world, pos, true);
            sendPacket(state, (ServerLevel) world, pos, true);
        }
    }

    private void turnOFF(BlockState state, Level world, BlockPos pos, Player player) {
        world.playSound(player, pos, SoundEventRegistry.RADIO_CLICK.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        if (!world.isClientSide) {
            pressButton(state, world, pos, false);
            sendPacket(state, (ServerLevel) world, pos, false);
        }
    }

    public int tune(Level world, BlockState blockState, BlockPos blockPos, int scrollValue) {
        if (scrollValue % CHANNELS == 0) {
            return blockState.getValue(CHANNEL);
        }
        int currentChannel = blockState.getValue(CHANNEL);
        int newChannel = scrollValue < 0 ? (CHANNELS - (Math.abs(currentChannel + scrollValue) % CHANNELS)) % CHANNELS : (currentChannel + scrollValue) % CHANNELS;
        world.setBlock(blockPos, blockState.setValue(CHANNEL, newChannel).setValue(SEARCHING, true), 3);
        world.scheduleTick(blockPos, this, DELAY);
        return newChannel;
    }

    private void pressButton(BlockState state, Level world, BlockPos pos, boolean on) {
            world.setBlock(pos, state.setValue(ON, on).setValue(SEARCHING, true), 3);
            world.scheduleTick(pos, this, DELAY);
    }

    private void sendPacket(BlockState state, ServerLevel world, BlockPos pos, boolean on) {
        for (ServerPlayer player : world.players()) {
            TurnRadioS2CPacket packet = new TurnRadioS2CPacket(pos, state.getValue(CHANNEL), on);
            NetworkManager.sendToPlayer(player, packet);
        }
    }

    public static RegistryFriendlyByteBuf createPacketBuf(RegistryAccess access){
        return new RegistryFriendlyByteBuf(Unpooled.buffer(), access);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if(!world.isClientSide) {
                sendPacket(state, (ServerLevel) world, pos, false);
            }
            super.onRemove(state, world, pos, newState, moved);
        }

    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(SEARCHING)) {
            world.setBlock(pos, state.setValue(SEARCHING, false), 3);
        }
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(ON)) {
            spawnParticles(world, pos);
        }
    }

    private static void spawnParticles(Level world, BlockPos pos) {
        RandomSource random = world.random;

        double d = (double) pos.getX() + random.nextDouble();
        double e = pos.getY() + 0.5 + random.nextDouble();
        double f = (double) pos.getZ() + random.nextDouble();

        world.addParticle(ParticleTypes.NOTE, d, e, f, 0.0, 0.0, 0.0);

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON, CHANNEL, SEARCHING);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.canbeplaced").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    static {
        ON = BooleanProperty.create("on");
        CHANNEL = IntegerProperty.create("channel", 0, CHANNELS - 1);
        SEARCHING = BooleanProperty.create("searching");
    }
}
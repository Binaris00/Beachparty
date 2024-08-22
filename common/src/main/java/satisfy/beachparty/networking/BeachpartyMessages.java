package satisfy.beachparty.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import satisfy.beachparty.block.RadioBlock;
import satisfy.beachparty.networking.packet.MouseScrollC2SPacket;
import satisfy.beachparty.networking.packet.TuneRadioS2CPacket;
import satisfy.beachparty.networking.packet.TurnRadioS2CPacket;
import satisfy.beachparty.util.RadioHelper;

import java.util.List;

public class BeachpartyMessages {

    public static void registerC2SPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, MouseScrollC2SPacket.PACKET_ID, MouseScrollC2SPacket.PACKET_CODEC, (payload, context) -> {
            Level serverWorld = context.getPlayer().level();
            BlockPos blockPos = payload.pos();
            int scrollValue = payload.scrollValue();

            context.queue(() -> {
                BlockState blockState = serverWorld.getBlockState(blockPos);

                if (blockState.getBlock() instanceof RadioBlock radioBlock) {
                    if (!blockState.getValue(RadioBlock.ON) || blockState.getValue(RadioBlock.SEARCHING)) {
                        return;
                    }

                    int channel = radioBlock.tune(serverWorld, blockState, blockPos, scrollValue);

                    List<ServerPlayer> serverPlayerEntities = context.getPlayer().getServer().getPlayerList().getPlayers();
                    for (ServerPlayer serverPlayer : serverPlayerEntities) {
                        TuneRadioS2CPacket packet = new TuneRadioS2CPacket(blockPos, channel);
                        NetworkManager.sendToPlayer(serverPlayer, packet);
                    }
                }
            });
        });
    }

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TuneRadioS2CPacket.PACKET_ID, TuneRadioS2CPacket.PACKET_CODEC, (payload, context) -> {
            BlockPos blockPos = payload.pos();
            int channel = payload.channel();
            context.queue(() -> RadioHelper.tune(blockPos, channel));
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TurnRadioS2CPacket.PACKET_ID, TurnRadioS2CPacket.PACKET_CODEC, (payload, context) -> {
            BlockPos blockPos = payload.pos();
            int channel = payload.channel();
            boolean on = payload.on();
            context.queue(() -> RadioHelper.setPlaying(blockPos, channel, on, on ? RadioBlock.DELAY : 0));
        });
    }
}

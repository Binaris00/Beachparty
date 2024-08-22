package satisfy.beachparty.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.BeachpartyIdentifier;

public record TurnRadioS2CPacket(BlockPos pos, int channel, boolean on) implements CustomPacketPayload {
    public static final ResourceLocation TURN_RADIO = BeachpartyIdentifier.of("turn_radio");
    public static final CustomPacketPayload.Type<TurnRadioS2CPacket> PACKET_ID = new CustomPacketPayload.Type<>(TURN_RADIO);

    public static final StreamCodec<RegistryFriendlyByteBuf, TurnRadioS2CPacket> PACKET_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, TurnRadioS2CPacket::pos,
            ByteBufCodecs.INT, TurnRadioS2CPacket::channel,
            ByteBufCodecs.BOOL,TurnRadioS2CPacket::on,
            TurnRadioS2CPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}

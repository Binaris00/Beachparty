package satisfy.beachparty.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.BeachpartyIdentifier;

public record TuneRadioS2CPacket(BlockPos pos, int channel) implements CustomPacketPayload {
    public static final ResourceLocation TUNE_RADIO = BeachpartyIdentifier.of("tune_radio");
    public static final CustomPacketPayload.Type<TuneRadioS2CPacket> PACKET_ID = new CustomPacketPayload.Type<>(TUNE_RADIO);

    public static final StreamCodec<RegistryFriendlyByteBuf, TuneRadioS2CPacket> PACKET_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, TuneRadioS2CPacket::pos,
            ByteBufCodecs.INT, TuneRadioS2CPacket::channel,
            TuneRadioS2CPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}

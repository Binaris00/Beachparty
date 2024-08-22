package satisfy.beachparty.networking.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.BeachpartyIdentifier;

public record MouseScrollC2SPacket(BlockPos pos, int scrollValue) implements CustomPacketPayload {
    public static final ResourceLocation MOUSE_SCROLL = BeachpartyIdentifier.of("mouse_scroll_c2s");
    public static final CustomPacketPayload.Type<MouseScrollC2SPacket> PACKET_ID = new CustomPacketPayload.Type<>(MOUSE_SCROLL);

    public static final StreamCodec<RegistryFriendlyByteBuf, MouseScrollC2SPacket> PACKET_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, MouseScrollC2SPacket::pos,
            ByteBufCodecs.INT, MouseScrollC2SPacket::scrollValue,
            MouseScrollC2SPacket::new
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}

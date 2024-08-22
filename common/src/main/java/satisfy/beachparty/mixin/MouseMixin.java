package satisfy.beachparty.mixin;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import satisfy.beachparty.block.RadioBlock;
import satisfy.beachparty.networking.packet.MouseScrollC2SPacket;
import satisfy.beachparty.registry.ObjectRegistry;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void MouseScrollOnRadio(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == Minecraft.getInstance().getWindow().getWindow()) {
            Minecraft client = Minecraft.getInstance();
            if(client.hitResult instanceof BlockHitResult blockHitResult){

                if (blockHitResult.getType() != HitResult.Type.BLOCK) return;

                BlockPos blockPos = blockHitResult.getBlockPos();
                if (client.level == null) return;
                BlockState blockState = client.level.getBlockState(blockPos);
                if (blockState.getBlock() != ObjectRegistry.RADIO.get() || !blockState.getValue(RadioBlock.ON)) return;

                int scrollValue = (int) beachparty$calculateScrollValue(vertical, client.options);
                beachparty$handleScrollEvent(blockPos, scrollValue, client.level.registryAccess());
                ci.cancel();
            }
        }
    }

    @Unique
    private double beachparty$calculateScrollValue(double vertical, Options options) {
        return options.discreteMouseScroll().get() ? Math.signum(vertical) : vertical * options.mouseWheelSensitivity().get();
    }

    @Unique
    private void beachparty$handleScrollEvent(BlockPos blockPos, int scrollValue, RegistryAccess access) {
        if (scrollValue == 0) {
            return;
        }

        MouseScrollC2SPacket packet = new MouseScrollC2SPacket(blockPos, scrollValue);
        NetworkManager.sendToServer(packet);
    }
}

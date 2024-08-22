package satisfy.beachparty.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import satisfy.beachparty.Beachparty;
import satisfy.beachparty.client.BeachPartyClient;
import satisfy.beachparty.client.gui.MiniFridgeGui;
import satisfy.beachparty.client.gui.TikiBarGui;
import satisfy.beachparty.registry.ScreenHandlerTypesRegistry;

@EventBusSubscriber(modid = Beachparty.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BeachpartyClientForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BeachPartyClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BeachPartyClient.initClient();
    }

    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(ScreenHandlerTypesRegistry.TIKI_BAR_GUI_HANDLER.get(), TikiBarGui::new);
        event.register(ScreenHandlerTypesRegistry.MINI_FRIDGE_GUI_HANDLER.get(), MiniFridgeGui::new);
    }
}

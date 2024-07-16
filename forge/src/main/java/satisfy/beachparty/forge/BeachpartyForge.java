package satisfy.beachparty.forge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import satisfy.beachparty.Beachparty;
import satisfy.beachparty.forge.registry.VillagersForge;
import satisfy.beachparty.registry.CompostablesRegistry;

@Mod(Beachparty.MOD_ID)
public class BeachpartyForge {
    public BeachpartyForge(IEventBus modBus) {
        Beachparty.init();
        VillagersForge.register(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
        Beachparty.commonSetup();
    }


}

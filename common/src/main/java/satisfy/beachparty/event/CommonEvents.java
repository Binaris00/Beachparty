package satisfy.beachparty.event;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import satisfy.beachparty.util.BeachpartyLoottableInjector;

public class CommonEvents {

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
    }

    private static void onModifyLootTable(ResourceKey<LootTable> lootTableResourceKey, LootEvent.LootTableModificationContext lootTableModificationContext, boolean b) {
        BeachpartyLoottableInjector.InjectLoot(lootTableResourceKey.location(), lootTableModificationContext);
    }

    private static void onModifyLootTable(ResourceLocation id, LootEvent.LootTableModificationContext ctx) {
        BeachpartyLoottableInjector.InjectLoot(id, ctx);
    }
}

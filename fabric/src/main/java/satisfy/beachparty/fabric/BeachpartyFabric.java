package satisfy.beachparty.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import satisfy.beachparty.Beachparty;
import satisfy.beachparty.fabric.registry.VillagersFabric;
import satisfy.beachparty.fabric.world.BeachpartyBiomeModification;
import satisfy.beachparty.registry.CompostablesRegistry;

import java.util.Set;

public class BeachpartyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Beachparty.init();
        CompostablesRegistry.init();
        Beachparty.commonSetup();
        BeachpartyBiomeModification.init();
        VillagersFabric.init();
        registerLootTable();
    }





    protected static void registerLootTable() {
        Set<ResourceKey<LootTable>> chestsId = Set.of(
                BuiltInLootTables.BURIED_TREASURE);

        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            ResourceLocation id = key.location();
            ResourceLocation injectId = ResourceLocation.fromNamespaceAndPath(Beachparty.MOD_ID, "inject/" + id.getPath());
            if (chestsId.contains(key)) {
                tableBuilder.pool(LootPool.lootPool().add(NestedLootTable.lootTableReference(key).setWeight(1).setQuality(0)).build());
            }
        });

    }

}

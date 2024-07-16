package satisfy.beachparty.util;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import satisfy.beachparty.Beachparty;

public class BeachpartyLoottableInjector {
        public static void InjectLoot(ResourceLocation id, LootEvent.LootTableModificationContext context)
        {
            String prefix = "minecraft:chests/";
            String name = id.toString();

            if (name.startsWith(prefix))
            {
                String file = name.substring(name.indexOf(prefix) + prefix.length());
                switch (file) {
                    case "desert_pyramid", "buried_treasure", "shipwreck_supply", "shipwreck_treasure", "simple_dungeon", "underwater_ruin_big", "underwater_ruin_small", "woodland_mansion",
                            "village/village_cartographer", "village/plains_house", "village_savanna_house" ->
                            context.addPool(getPool(file));
                    default -> {}
                }
            }
        }

        public static LootPool.Builder getPool(String entryName)
        {
            return LootPool.lootPool().add(getPoolEntry(entryName));
        }

        @SuppressWarnings("rawtypes")
        private static LootPoolEntryContainer.Builder getPoolEntry(String name)
        {
            ResourceLocation table = ResourceLocation.fromNamespaceAndPath(Beachparty.MOD_ID, "chests/" + name);
            ResourceKey.create(Registries.LOOT_TABLE, table);

            return NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, table));
        }
    }


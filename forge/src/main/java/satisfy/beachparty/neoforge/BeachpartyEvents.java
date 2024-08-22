package satisfy.beachparty.neoforge;

import de.cristelknight.doapi.common.util.VillagerUtil;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import satisfy.beachparty.Beachparty;
import satisfy.beachparty.neoforge.registry.VillagersForge;
import satisfy.beachparty.registry.ObjectRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeachpartyEvents {
    @EventBusSubscriber(modid = Beachparty.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event){
            if(event.getType().equals(VillagersForge.BARKEEPER.get())){
                Map<Integer, List<VillagerTrades.ItemListing>> trades = event.getTrades();
                addBarkeeperTrades(trades);
            }
            else if(event.getType().equals(VillagersForge.BEACH_GUY.get())){
                Map<Integer, List<VillagerTrades.ItemListing>> trades = event.getTrades();
                addBeachGuyTrades(trades);
            }
        }
    }

    private static void addBarkeeperTrades(Map<Integer, List<VillagerTrades.ItemListing>> trades){
        List<VillagerTrades.ItemListing> barkeeperTrades = trades.getOrDefault(1, new ArrayList<>());
        barkeeperTrades.add(new VillagerUtil.SellItemFactory(Blocks.ICE, 1, 2, 5));
        barkeeperTrades.add(new VillagerUtil.SellItemFactory(Items.SNOWBALL, 1, 8, 5));
        barkeeperTrades.add(new VillagerUtil.BuyForOneEmeraldFactory(Items.WATER_BUCKET, 12, 1, 5));
        barkeeperTrades.add(new VillagerUtil.BuyForOneEmeraldFactory(Items.POWDER_SNOW_BUCKET, 12, 1, 5));
        trades.put(1, barkeeperTrades);
        trades.put(2, new ArrayList<>());
        trades.put(3, new ArrayList<>());
        trades.put(4, new ArrayList<>());
        trades.put(5, new ArrayList<>());
    }

    private static void addBeachGuyTrades(Map<Integer, List<VillagerTrades.ItemListing>> trades){
        List<VillagerTrades.ItemListing> beachGuyTrades = trades.getOrDefault(1, new ArrayList<>());
        beachGuyTrades.add(new VillagerUtil.SellItemFactory(ObjectRegistry.SUNGLASSES.get(), 18, 1, 5));
        beachGuyTrades.add(new VillagerUtil.SellItemFactory(ObjectRegistry.BIKINI.get(), 12, 1, 5));
        beachGuyTrades.add(new VillagerUtil.SellItemFactory(ObjectRegistry.TRUNKS.get(), 8, 1, 5));
        beachGuyTrades.add(new VillagerUtil.SellItemFactory(ObjectRegistry.SWIM_WINGS.get(), 5, 1, 5));
        trades.put(1, beachGuyTrades);
        trades.put(2, new ArrayList<>());
        trades.put(3, new ArrayList<>());
        trades.put(4, new ArrayList<>());
        trades.put(5, new ArrayList<>());
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() == ObjectRegistry.RADIO.get()) {
            event.setCanceled(true);
        }
    }
}
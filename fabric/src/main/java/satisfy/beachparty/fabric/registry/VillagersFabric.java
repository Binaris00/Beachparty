package satisfy.beachparty.fabric.registry;

import de.cristelknight.doapi.common.util.VillagerUtil;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import satisfy.beachparty.BeachpartyIdentifier;
import satisfy.beachparty.registry.ObjectRegistry;

public class VillagersFabric {

    private static final ResourceLocation BEACH_GUY_POI_IDENTIFIER = BeachpartyIdentifier.of("beach_guy_poi");
    public static final PoiType BEACH_GUY_POI = PointOfInterestHelper.register(BEACH_GUY_POI_IDENTIFIER, 1, 12, ObjectRegistry.LOUNGE_CHAIR.get());
    public static final VillagerProfession BEACH_GUY = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, ResourceLocation.fromNamespaceAndPath("beachparty", "beach_guy"), VillagerProfessionBuilder.create().id(ResourceLocation.fromNamespaceAndPath("beachparty", "beach_guy")).workstation(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, BEACH_GUY_POI_IDENTIFIER)).build());

    private static final ResourceLocation BARKEEPER_POI_IDENTIFIER = BeachpartyIdentifier.of("barkeeper_poi");
    public static final PoiType BARKEEPER_POI = PointOfInterestHelper.register(BARKEEPER_POI_IDENTIFIER, 1, 12, ObjectRegistry.TIKI_BAR.get());
    public static final VillagerProfession BARKEEPER = Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, ResourceLocation.fromNamespaceAndPath("beachparty", "barkeeper"), VillagerProfessionBuilder.create().id(ResourceLocation.fromNamespaceAndPath("beachparty", "barkeeper")).workstation(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, BARKEEPER_POI_IDENTIFIER)).build());

    public static final VillagerType BEACH = Registry.register(BuiltInRegistries.VILLAGER_TYPE, ResourceLocation.fromNamespaceAndPath("beachparty", "beachparty"), new VillagerType("beachparty"));


    
    public static void init() {
        TradeOfferHelper.registerVillagerOffers(BEACH_GUY, 1, factories -> {
            factories.add(new VillagerUtil.SellItemFactory(ObjectRegistry.SUNGLASSES.get(), 18, 1, 5));
            factories.add(new VillagerUtil.SellItemFactory(ObjectRegistry.BIKINI.get(), 12, 1, 5));
            factories.add(new VillagerUtil.SellItemFactory(ObjectRegistry.TRUNKS.get(), 8, 1, 5));
            factories.add(new VillagerUtil.SellItemFactory(ObjectRegistry.SWIM_WINGS.get(), 5, 1, 5));
        });

            TradeOfferHelper.registerVillagerOffers(BARKEEPER, 1, factories -> {
                factories.add(new VillagerUtil.SellItemFactory(Blocks.ICE, 1, 2, 5));
                factories.add(new VillagerUtil.SellItemFactory(Items.SNOWBALL, 1, 8, 5));
                factories.add(new VillagerUtil.BuyForOneEmeraldFactory(Items.WATER_BUCKET, 12, 1, 5));
                factories.add(new VillagerUtil.BuyForOneEmeraldFactory(Items.POWDER_SNOW_BUCKET, 12, 1, 5));
        });

        VillagerType.BY_BIOME.put(ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("beach")), BEACH);

    }
}
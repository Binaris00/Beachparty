package satisfy.beachparty.forge.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import satisfy.beachparty.Beachparty;
import satisfy.beachparty.registry.ObjectRegistry;

import java.lang.reflect.InvocationTargetException;

public class VillagersForge {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Beachparty.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, Beachparty.MOD_ID);

    public static final DeferredRegister<VillagerType> VILLAGER_TYPES = DeferredRegister.create(Registries.VILLAGER_TYPE, Beachparty.MOD_ID);


    public static final DeferredHolder<PoiType, PoiType> BEACH_GUY_POI = POI_TYPES.register("beach_guy_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.LOUNGE_CHAIR.get().getStateDefinition().getPossibleStates()), 1, 12));
    public static final DeferredHolder<VillagerProfession, VillagerProfession> BEACH_GUY = VILLAGER_PROFESSIONS.register("beach_guy", () ->
            new VillagerProfession("beach_guy", x -> x.value() == BEACH_GUY_POI.get(), x -> x.value() == BEACH_GUY_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER));


    public static final DeferredHolder<PoiType, PoiType> BARKEEPER_POI = POI_TYPES.register("barkeeper_poi", () ->
            new PoiType(ImmutableSet.copyOf(ObjectRegistry.TIKI_BAR.get().getStateDefinition().getPossibleStates()), 1, 12));
    public static final DeferredHolder<VillagerProfession, VillagerProfession> BARKEEPER = VILLAGER_PROFESSIONS.register("barkeeper", () ->
            new VillagerProfession("beach_guy", x -> x.value() == BARKEEPER_POI.get(), x -> x.value() == BARKEEPER_POI.value(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER));


    public static final DeferredHolder<VillagerType, VillagerType> BEACH = VILLAGER_TYPES.register("beachparty", () -> new VillagerType("beachparty"));


    public static void registerPOIs(){
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, BARKEEPER_POI.get(), BEACH_GUY_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
        }


    }

    public static void register(IEventBus eventBus) {

        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
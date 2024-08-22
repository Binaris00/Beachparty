package satisfy.beachparty.registry;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumMap;
import java.util.List;

import static satisfy.beachparty.Beachparty.MOD_ID;

public class ArmorMaterialRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(MOD_ID, Registries.ARMOR_MATERIAL);
    private static final int ENCHANTMENT_VALUE = 15;
    private static final Holder<SoundEvent> EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    private static final float TOUGHNESS = 0.0F;
    private static final float KNOCKBACK_RESISTANCE = 0.0F;

    private static Holder<ArmorMaterial> createMaterial(String name, Ingredient repairIngredient) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 1);
                    map.put(ArmorItem.Type.LEGGINGS, 1);
                    map.put(ArmorItem.Type.CHESTPLATE, 1);
                    map.put(ArmorItem.Type.HELMET, 1);
                    map.put(ArmorItem.Type.BODY, 1);
                }),

                ENCHANTMENT_VALUE,

                EQUIP_SOUND,

                () -> repairIngredient,

                List.of(
                    new ArmorMaterial.Layer(
                        ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
                    ),
                    new ArmorMaterial.Layer(
                        ResourceLocation.fromNamespaceAndPath(MOD_ID, name), "_overlay", true
                    )
                ),

                TOUGHNESS,

                KNOCKBACK_RESISTANCE
        ));
    }

    private static Holder<ArmorMaterial> createMaterialNoOverlay(String name, Ingredient repairIngredient) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 1);
                    map.put(ArmorItem.Type.LEGGINGS, 1);
                    map.put(ArmorItem.Type.CHESTPLATE, 1);
                    map.put(ArmorItem.Type.HELMET, 1);
                    map.put(ArmorItem.Type.BODY, 1);
                }),

                ENCHANTMENT_VALUE,

                EQUIP_SOUND,

                () -> repairIngredient,

                List.of(
                        new ArmorMaterial.Layer(
                                ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
                        )
                ),

                TOUGHNESS,

                KNOCKBACK_RESISTANCE
        ));
    }


    public static final Holder<ArmorMaterial> TRUNKS = createMaterial("trunks", Ingredient.of(Items.STRING));
    public static final Holder<ArmorMaterial> BIKINI = createMaterial("bikini", Ingredient.of(Items.STRING));
    public static final Holder<ArmorMaterial> RING = createMaterial("ring", Ingredient.of(Items.DRIED_KELP));
    public static final Holder<ArmorMaterial> BEACH_HAT = createMaterial("beach_hat", Ingredient.of(Items.WHEAT));
    public static final Holder<ArmorMaterial> SUNGLASSES = createMaterialNoOverlay("sunglasses", Ingredient.of(Blocks.BLACK_STAINED_GLASS_PANE.asItem()));
    public static final Holder<ArmorMaterial> SWIM_WINGS = createMaterialNoOverlay("swim_wings", Ingredient.of(Items.DRIED_KELP));
    public static final Holder<ArmorMaterial> CROCS = createMaterial("crocs", Ingredient.of(Items.DRIED_KELP));
}

package satisfy.beachparty.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import satisfy.beachparty.registry.ArmorMaterialRegistry;

import java.util.Map;
import java.util.Objects;

public interface IBeachpartyArmorSet {

    Map<Holder<ArmorMaterial>, MobEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<Holder<ArmorMaterial>, MobEffectInstance>())
                    .put(ArmorMaterialRegistry.BIKINI, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 14 * 20, 1))
                    .put(ArmorMaterialRegistry.TRUNKS, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 14 * 20, 1))
                    .put(ArmorMaterialRegistry.SWIM_WINGS, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 14 * 20, 0))
                    .put(ArmorMaterialRegistry.RING, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 14 * 20, 1)).build();

    default boolean hasSwimwearSet(Player player) {
        return hasSwimearBoots(player) && hasSwimearLeggings(player) && hasSwimwearBreastplate(player) && hasSwimearHelmet(player);
    }

    default void checkForSet(Player player) {
        if (hasSwimwearSet(player)) {
            addStatusEffectForMaterial(player, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 14 * 20, 2));
            addStatusEffectForMaterial(player, new MobEffectInstance(MobEffects.WATER_BREATHING, 14 * 20, 0));
        }
        hasSwimwear(player);
    }

    default void hasSwimwear(Player player) {
        for (Map.Entry<Holder<ArmorMaterial>, MobEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            Holder<ArmorMaterial> mapArmorMaterial = entry.getKey();
            MobEffectInstance mapStatusEffect = entry.getValue();

            if (hasCorrectSwimWear(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapStatusEffect);
            }
        }
    }

    private boolean hasCorrectSwimWear(Holder<ArmorMaterial> material, Player player) {
        if (material.equals(ArmorMaterialRegistry.BIKINI) || material.equals(ArmorMaterialRegistry.TRUNKS)) {
            int slot = 1;
            if (!player.getInventory().getArmor(slot).isEmpty()) {
                ArmorItem armor = (ArmorItem) player.getInventory().getArmor(slot).getItem();
                return armor.getMaterial() == material;
            }
            return false;
        }
        if (material.equals(ArmorMaterialRegistry.SWIM_WINGS) || material.equals(ArmorMaterialRegistry.RING)) {
            int slot = 2;
            if (!player.getInventory().getArmor(slot).isEmpty()) {
                if (player.getInventory().getArmor(slot).getItem() instanceof ArmorItem armor) {
                    return armor.getMaterial() == material;
                }
            }
            return false;
        }
        return false;
    }

    default void addStatusEffectForMaterial(Player player, MobEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

        if (!hasPlayerEffect || Objects.requireNonNull(player.getEffect(mapStatusEffect.getEffect())).getDuration() < 11 * 20) {
            player.addEffect(new MobEffectInstance(mapStatusEffect.getEffect(),
                    mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier(), true, false, true));
        }
    }

    static boolean hasSwimearBoots(Player player) {
        if (player.getInventory().getArmor(0).isEmpty()) return false;
        Item item = player.getInventory().getArmor(0).getItem();
        if (item instanceof ArmorItem armorItem) {
            return isSwimwearBoots(armorItem.getMaterial());
        }
        return false;
    }

    private static boolean isSwimwearBoots(Holder<ArmorMaterial> armorItem) {
        return armorItem == ArmorMaterialRegistry.CROCS;
    }

    static boolean hasSwimearLeggings(Player player) {
        if (player.getInventory().getArmor(1).isEmpty()) return false;
        Item item = player.getInventory().getArmor(1).getItem();
        if (item instanceof ArmorItem armorItem) {
            return isSwimwearLeggings(armorItem.getMaterial());
        }
        return false;
    }

    private static boolean isSwimwearLeggings(Holder<ArmorMaterial> armorItem) {
        return armorItem == ArmorMaterialRegistry.TRUNKS || armorItem == ArmorMaterialRegistry.BIKINI;
    }

    static boolean hasSwimwearBreastplate(Player player) {
        if (player.getInventory().getArmor(2).isEmpty()) return false;
        Item item = player.getInventory().getArmor(2).getItem();
        if (item instanceof ArmorItem armorItem) {
            return isSwimwearBreastplate(armorItem);
        }
        return false;
    }

    private static boolean isSwimwearBreastplate(ArmorItem armorItem) {
        return armorItem.getMaterial() == ArmorMaterialRegistry.RING || armorItem.getMaterial() == ArmorMaterialRegistry.SWIM_WINGS;
    }

    static boolean hasSwimearHelmet(Player player) {
        if (player.getInventory().getArmor(3).isEmpty()) return false;
        Item item = player.getInventory().getArmor(3).getItem();
        if (item instanceof ArmorItem armorItem) {
            return isSwimwearHelmet(armorItem.getMaterial());
        }
        return false;
    }


    private static boolean isSwimwearHelmet(Holder<ArmorMaterial> armorItem) {
        return armorItem == ArmorMaterialRegistry.BEACH_HAT || armorItem == ArmorMaterialRegistry.SUNGLASSES;
    }
}

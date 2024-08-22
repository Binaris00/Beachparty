package satisfy.beachparty.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import satisfy.beachparty.item.IBeachpartyArmorSet;

public class BeachpartyCustomArmorItem extends BetterCustomArmorModelItem implements IBeachpartyArmorSet {
    public BeachpartyCustomArmorItem(Holder<ArmorMaterial> material, Type slot, Properties settings, ResourceLocation texture) {
        super(material, slot, settings, texture, -0.7f);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide()) {
            if (entity instanceof Player player) {
                checkForSet(player);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}

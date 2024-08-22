package satisfy.beachparty.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import satisfy.beachparty.item.IBeachpartyArmorSet;
import satisfy.beachparty.registry.ArmorRegistry;

import java.util.List;

public class BeachpartyArmorItem extends ArmorItem implements IBeachpartyArmorSet {
    public BeachpartyArmorItem(Holder<ArmorMaterial> material, Type slot, Properties settings) {
        super(material, slot, settings);
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

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ArmorRegistry.appendTooltip(list);
    }
}

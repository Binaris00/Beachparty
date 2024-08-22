package satisfy.beachparty.item.armor;


import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import satisfy.beachparty.item.IBeachpartyArmorSet;
import satisfy.beachparty.registry.ArmorRegistry;

import java.util.List;


public class DyeableBeachpartyArmorItem extends ArmorItem implements IBeachpartyArmorSet {
    private final int defaultColor;
    public DyeableBeachpartyArmorItem(Holder<ArmorMaterial> material, Type slot, int color, Item.Properties settings) {
        super(material, slot, settings);
        defaultColor = color;
    }

    public int getDefaultColor() {
        return defaultColor;
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

    public int getColor(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.DYED_COLOR, new DyedItemColor(defaultColor, false)).rgb();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.dyeable").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        ArmorRegistry.appendTooltip(list);
    }
}
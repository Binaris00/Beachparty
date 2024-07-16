package satisfy.beachparty.item.armor;


import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import satisfy.beachparty.item.IBeachpartyArmorSet;
import satisfy.beachparty.registry.ArmorRegistry;

import java.util.List;


public class DyeableBeachpartyArmorItem extends ArmorItem implements IBeachpartyArmorSet {
    private final int defaultColor;
    public DyeableBeachpartyArmorItem(ArmorMaterial material, Type slot, int color, Item.Properties settings) {
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



    @Override
    public int getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement("display");
        return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : this.defaultColor;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.dyeable").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        ArmorRegistry.appendTooltip(list);
    }
}
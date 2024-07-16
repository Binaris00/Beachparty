package satisfy.beachparty.item;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class PoolNoodleItem extends SwordItem {

    public PoolNoodleItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties properties) {
        super(toolMaterial, properties);
        createAttributes(toolMaterial, attackDamage, attackSpeed);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beachparty.dyeable").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}
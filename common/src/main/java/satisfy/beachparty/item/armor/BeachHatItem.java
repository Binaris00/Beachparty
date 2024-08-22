package satisfy.beachparty.item.armor;

import de.cristelknight.doapi.common.item.CustomHatItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import satisfy.beachparty.BeachpartyIdentifier;
import satisfy.beachparty.registry.ArmorMaterialRegistry;
import satisfy.beachparty.registry.ArmorRegistry;

import java.util.List;

public class BeachHatItem extends CustomHatItem {


    public BeachHatItem(Item.Properties settings) {
        super(ArmorMaterialRegistry.BEACH_HAT, Type.HELMET, settings);
    }

    @Override
    public ResourceLocation getTexture() {
        return BeachpartyIdentifier.of("textures/entity/beach_hat.png");
    }

    @Override
    public Float getOffset() {
        return -1.8f;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ArmorRegistry.appendTooltip(list);
    }
}

package satisfy.beachparty.item.armor;

import de.cristelknight.doapi.common.item.CustomHatItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import satisfy.beachparty.registry.ArmorRegistry;

import java.util.List;

public class BetterCustomArmorModelItem extends CustomHatItem {

    private final ResourceLocation textureLocation;

    private final float offset;

    public BetterCustomArmorModelItem(Holder<ArmorMaterial> material, Type slot, Properties settings, ResourceLocation textureLocation, float offset){
        super(material, slot, settings);
        this.textureLocation = textureLocation;
        this.offset = offset;
    }
    @Override
    public ResourceLocation getTexture() {
        return textureLocation;
    }

    @Override
    public Float getOffset() {
        return offset;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ArmorRegistry.appendTooltip(list);
    }
}

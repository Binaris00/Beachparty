package satisfy.beachparty.client.gui;

import de.cristelknight.doapi.client.recipebook.screen.AbstractRecipeBookGUIScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import satisfy.beachparty.BeachpartyIdentifier;
import satisfy.beachparty.client.gui.handler.TikiBarGuiHandler;
import satisfy.beachparty.client.recipebook.TikiBarRecipeBook;

public class TikiBarGui extends AbstractRecipeBookGUIScreen<TikiBarGuiHandler> {
    public static final ResourceLocation BG = BeachpartyIdentifier.of("textures/gui/tiki_bar_gui.png");

    public static final int ARROW_Y = 45;
    public static final int ARROW_X = 94;

    public static final int SHAKE_Y = 42;
    public static final int SHAKE_X = 96;

    public TikiBarGui(TikiBarGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, new TikiBarRecipeBook(), BG);
    }

    @Override
    protected void init() {
        this.titleLabelX += 2;
        this.titleLabelY -= 3;
        super.init();
    }


    protected void renderProgressArrow(GuiGraphics guiGraphics) {
        final int progressX = this.menu.getShakeXProgress();
        guiGraphics.blit(BG, leftPos + ARROW_X, topPos + ARROW_Y, 177, 26, progressX, 10);

        final int progressY = menu.slots.get(0).hasItem() ? 20 : this.menu.getShakeYProgress();
        guiGraphics.blit(BG, leftPos + SHAKE_X, topPos + SHAKE_Y - progressY, 179, 22 - progressY, 15, progressY);
    }
}


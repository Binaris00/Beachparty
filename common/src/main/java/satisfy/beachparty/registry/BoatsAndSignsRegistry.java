package satisfy.beachparty.registry;

import de.cristelknight.doapi.DoApiCommonEP;
import de.cristelknight.doapi.terraform.boat.TerraformBoatType;
import de.cristelknight.doapi.terraform.boat.item.TerraformBoatItemHelper;
import de.cristelknight.doapi.terraform.sign.TerraformSignHelper;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import satisfy.beachparty.BeachpartyIdentifier;

public class BoatsAndSignsRegistry {

    public static ResourceLocation PALM_BOAT_TYPE = BeachpartyIdentifier.of("palm");
    public static ResourceLocation FLOATY_BOAT_TYPE = BeachpartyIdentifier.of("floaty");

    public static final ResourceLocation PALM_SIGN_TEXTURE = BeachpartyIdentifier.of("entity/signs/palm");


    public static final ResourceLocation PALM_HANGING_SIGN_TEXTURE = BeachpartyIdentifier.of("entity/signs/hanging/palm");
    public static final ResourceLocation PALM_HANGING_SIGN_GUI_TEXTURE = BeachpartyIdentifier.of("textures/gui/hanging_signs/palm");

    public static final RegistrySupplier<Block> PALM_SIGN = ObjectRegistry.registerWithoutItem("palm_sign", () -> TerraformSignHelper.getSign(PALM_SIGN_TEXTURE));
    public static final RegistrySupplier<Block> PALM_WALL_SIGN = ObjectRegistry.registerWithoutItem("palm_wall_sign", () -> TerraformSignHelper.getWallSign(PALM_SIGN_TEXTURE));
    public static final RegistrySupplier<Item> PALM_SIGN_ITEM = ObjectRegistry.registerItem("palm_sign", () -> new SignItem(ObjectRegistry.getSettings().stacksTo(16), PALM_SIGN.get(), PALM_WALL_SIGN.get()));
    public static final RegistrySupplier<Block> PALM_HANGING_SIGN = ObjectRegistry.registerWithoutItem("palm_hanging_sign", () -> TerraformSignHelper.getHangingSign(PALM_HANGING_SIGN_TEXTURE, PALM_HANGING_SIGN_GUI_TEXTURE));
    public static final RegistrySupplier<Block> PALM_WALL_HANGING_SIGN = ObjectRegistry.registerWithoutItem("palm_wall_hanging_sign", () -> TerraformSignHelper.getWallHangingSign(PALM_HANGING_SIGN_TEXTURE, PALM_HANGING_SIGN_GUI_TEXTURE));
    public static final RegistrySupplier<Item> PALM_HANGING_SIGN_ITEM = ObjectRegistry.registerItem("palm_hanging_sign", () -> new HangingSignItem(PALM_HANGING_SIGN.get(), PALM_WALL_HANGING_SIGN.get(), ObjectRegistry.getSettings().stacksTo(16)));

    public static RegistrySupplier<Item> PALM_BOAT = TerraformBoatItemHelper.registerBoatItem(ObjectRegistry.ITEMS, "palm_boat", PALM_BOAT_TYPE, false);
    public static RegistrySupplier<Item> PALM_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(ObjectRegistry.ITEMS, "palm_chest_boat", PALM_BOAT_TYPE, true);
    public static RegistrySupplier<Item> FLOATY = TerraformBoatItemHelper.registerBoatItem(ObjectRegistry.ITEMS, "floaty", FLOATY_BOAT_TYPE, false);
    public static RegistrySupplier<Item> FLOATY_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(ObjectRegistry.ITEMS, "floaty_chest_boat", FLOATY_BOAT_TYPE, true);

    public static void init() {

        DoApiCommonEP.registerBoatType(PALM_BOAT_TYPE, new TerraformBoatType.Builder().item(PALM_BOAT).chestItem(PALM_CHEST_BOAT).build());
        DoApiCommonEP.registerBoatType(FLOATY_BOAT_TYPE, new TerraformBoatType.Builder().item(FLOATY).chestItem(FLOATY_CHEST_BOAT).build());
    }
}

package satisfy.beachparty;

import net.minecraft.resources.ResourceLocation;

public final class BeachpartyIdentifier {

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Beachparty.MOD_ID, path);
    }

    public static ResourceLocation of(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}

package satisfy.beachparty.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import satisfy.beachparty.BeachpartyIdentifier;

public class TagRegistry {
    public static final TagKey<Biome> WARM_BIOME = TagKey.create(Registries.BIOME, BeachpartyIdentifier.of("warm_biome"));
}

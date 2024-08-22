package satisfy.beachparty.block.entity;

import com.mojang.serialization.MapCodec;
import de.cristelknight.doapi.common.block.CabinetBlock;
import de.cristelknight.doapi.common.registry.DoApiSoundEventRegistry;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CabinetBlockImp extends CabinetBlock {
    public static final MapCodec<CabinetBlockImp> CODEC = simpleCodec(CabinetBlockImp::new);

    public CabinetBlockImp() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS), DoApiSoundEventRegistry.CABINET_OPEN, DoApiSoundEventRegistry.CABINET_CLOSE);
    }

    public CabinetBlockImp(Properties properties) {
        super(properties, DoApiSoundEventRegistry.CABINET_OPEN, DoApiSoundEventRegistry.CABINET_CLOSE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}

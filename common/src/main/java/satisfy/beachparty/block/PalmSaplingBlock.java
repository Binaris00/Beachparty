package satisfy.beachparty.block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.world.ConfiguredFeatures;

import java.util.Optional;

public class PalmSaplingBlock extends SaplingBlock {
    public PalmSaplingBlock() {
        super(PALM, Properties.ofFullCopy(Blocks.ACACIA_SAPLING).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
    }


    public static final TreeGrower PALM = new TreeGrower(
            "oak",
            Optional.of(ConfiguredFeatures.PALM_TREE_KEY),
            Optional.empty(),
            Optional.empty());

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.SAND);
    }
}


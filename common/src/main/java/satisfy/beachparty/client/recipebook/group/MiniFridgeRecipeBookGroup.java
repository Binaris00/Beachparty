package satisfy.beachparty.client.recipebook.group;

import com.google.common.collect.ImmutableList;
import de.cristelknight.doapi.client.recipebook.IRecipeBookGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Blocks;
import satisfy.beachparty.recipe.MiniFridgeRecipe;

import java.util.List;

@Environment(EnvType.CLIENT)
public enum MiniFridgeRecipeBookGroup implements IRecipeBookGroup {
    SEARCH(new ItemStack(Items.COMPASS)),
    FRIDGE(new ItemStack(Blocks.ICE)),
    MISC(new ItemStack(Items.SNOWBALL));

    public static final List<IRecipeBookGroup> FRIDGE_GROUPS = ImmutableList.of(SEARCH, FRIDGE, MISC);

    private final List<ItemStack> icons;

    MiniFridgeRecipeBookGroup(ItemStack... entries) {
        this.icons = ImmutableList.copyOf(entries);
    }

    public boolean fitRecipe(Recipe<? extends RecipeInput> recipe, RegistryAccess registryAccess) {
        if (recipe instanceof MiniFridgeRecipe miniFridgeRecipe) {
            switch (this) {
                case SEARCH -> {
                    return true;
                }
                case FRIDGE -> {
                    if (miniFridgeRecipe.getIngredients().stream().anyMatch((ingredient) -> ingredient.test(Blocks.ICE.asItem().getDefaultInstance()))) {
                        return true;
                    }
                }
                case MISC -> {
                    if (miniFridgeRecipe.getIngredients().stream().noneMatch((ingredient) -> ingredient.test(Blocks.ICE.asItem().getDefaultInstance()))) {
                        return true;
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<ItemStack> getIcons() {
        return this.icons;
    }

}

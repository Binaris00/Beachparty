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
import satisfy.beachparty.recipe.TikiBarRecipe;
import satisfy.beachparty.registry.ObjectRegistry;

import java.util.List;

@Environment(EnvType.CLIENT)
public enum TikiBarRecipeBookGroup implements IRecipeBookGroup {
    SEARCH(new ItemStack(Items.COMPASS)),
    COCKTAIL(new ItemStack(ObjectRegistry.COCONUT_COCKTAIL.get())),
    MISC(new ItemStack(ObjectRegistry.COCONUT.get()));

    public static final List<IRecipeBookGroup> TIKI_GROUPS = ImmutableList.of(SEARCH, COCKTAIL, MISC);

    private final List<ItemStack> icons;

    TikiBarRecipeBookGroup(ItemStack... entries) {
        this.icons = ImmutableList.copyOf(entries);
    }

    public boolean fitRecipe(Recipe<? extends RecipeInput> recipe, RegistryAccess registryAccess) {
        if (recipe instanceof TikiBarRecipe tikiBarRecipe) {
            switch (this) {
                case SEARCH -> {
                    return true;
                }
                case COCKTAIL -> {
                    if (tikiBarRecipe.getIngredients().stream().anyMatch((ingredient) -> ingredient.test(Blocks.ICE.asItem().getDefaultInstance()))) {
                        return true;
                    }
                }
                case MISC -> {
                    if (tikiBarRecipe.getIngredients().stream().noneMatch((ingredient) -> ingredient.test(Blocks.ICE.asItem().getDefaultInstance()))) {
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

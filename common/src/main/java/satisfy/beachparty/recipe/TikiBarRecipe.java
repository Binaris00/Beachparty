package satisfy.beachparty.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cristelknight.doapi.common.util.GeneralUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import satisfy.beachparty.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TikiBarRecipe implements Recipe<RecipeInput> {
    final ResourceLocation id;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    public TikiBarRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
    }

    public TikiBarRecipe(Optional<ResourceLocation> id, List<Ingredient> inputs, ItemStack output) {
        this.id = id.orElseGet(() -> ResourceLocation.fromNamespaceAndPath("beachparty", "tiki_bar_recipe"));
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.addAll(inputs);
        this.inputs = nonNullList;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return GeneralUtil.matchesRecipe(recipeInput, inputs, 1, 2);
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.copy();
    }

    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TIKI_BAR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.TIKI_BAR_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<TikiBarRecipe> {
        public static final MapCodec<TikiBarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        ResourceLocation.CODEC.optionalFieldOf("id").forGetter(recipe -> Optional.of(recipe.getId())),
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(TikiBarRecipe::getIngredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output)
                ).apply(instance, TikiBarRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, TikiBarRecipe> STREAM_CODEC = StreamCodec.of(TikiBarRecipe.Serializer::toNetwork, TikiBarRecipe.Serializer::fromNetwork);

        public static TikiBarRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceLocation id = buf.readResourceLocation();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            var children = buf.readCollection(ArrayList::new, buffer -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            ingredients.addAll(children);
            final var output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);

            return new TikiBarRecipe(id, ingredients, output);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, TikiBarRecipe recipe) {
            buf.writeResourceLocation(recipe.getId());
            buf.writeCollection(recipe.getIngredients(), (b, child) -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, child));
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.output);
        }

        @Override
        public @NotNull MapCodec<TikiBarRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, TikiBarRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
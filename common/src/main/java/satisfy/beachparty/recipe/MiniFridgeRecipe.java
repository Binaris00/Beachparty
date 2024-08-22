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

public class MiniFridgeRecipe implements Recipe<RecipeInput> {

    final ResourceLocation id;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    public MiniFridgeRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
    }

    public MiniFridgeRecipe(Optional<ResourceLocation> id, List<Ingredient> inputs, ItemStack output) {
        this.id = id.orElseGet(() -> ResourceLocation.fromNamespaceAndPath("beachparty", "mini_fridge_recipe"));
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

    public @NotNull ItemStack result(){
        return this.output.copy();
    }


    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<MiniFridgeRecipe> {
        public static final MapCodec<MiniFridgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        ResourceLocation.CODEC.optionalFieldOf("id").forGetter(recipe -> Optional.of(recipe.getId())),
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(MiniFridgeRecipe::getIngredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output)
                ).apply(instance, MiniFridgeRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, MiniFridgeRecipe> STREAM_CODEC = StreamCodec.of(MiniFridgeRecipe.Serializer::toNetwork, MiniFridgeRecipe.Serializer::fromNetwork);


        public static MiniFridgeRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceLocation id = buf.readResourceLocation();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            var children = buf.readCollection(ArrayList::new, buffer -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            ingredients.addAll(children);
            final var output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);

            return new MiniFridgeRecipe(id, ingredients, output);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, MiniFridgeRecipe recipe) {
            buf.writeResourceLocation(recipe.getId());
            buf.writeCollection(recipe.getIngredients(), (b, child) -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, child));
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.output);
        }


        @Override
        public @NotNull MapCodec<MiniFridgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, MiniFridgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
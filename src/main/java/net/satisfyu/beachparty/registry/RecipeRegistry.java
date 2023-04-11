package net.satisfyu.beachparty.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.satisfyu.beachparty.BeachpartyIdentifier;
import net.satisfyu.beachparty.recipe.TikiBarRecipe;

import java.util.HashMap;
import java.util.Map;

public class RecipeRegistry {

    private static final Map<Identifier, RecipeSerializer<?>> RECIPE_SERIALIZERS = new HashMap<>();
    private static final Map<Identifier, RecipeType<?>> RECIPE_TYPES = new HashMap<>();
    public static final RecipeType<TikiBarRecipe> TIKI_BAR_RECIPE_RECIPE_TYPE = create("tiki_bar_mixing");
    public static final RecipeSerializer<TikiBarRecipe> TIKI_BAR_RECIPE_RECIPE_SERIALIZER = create("tiki_bar_mixing", new TikiBarRecipe.Serializer());


    private static <T extends Recipe<?>> RecipeSerializer<T> create(String name, RecipeSerializer<T> serializer) {
        RECIPE_SERIALIZERS.put(new BeachpartyIdentifier(name), serializer);
        return serializer;
    }

    private static <T extends Recipe<?>> RecipeType<T> create(String name) {
        final RecipeType<T> type = new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        RECIPE_TYPES.put(new BeachpartyIdentifier(name), type);
        return type;
    }

    public static void init() {
        for (Map.Entry<Identifier, RecipeSerializer<?>> entry : RECIPE_SERIALIZERS.entrySet()) {
            Registry.register(Registry.RECIPE_SERIALIZER, entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Identifier, RecipeType<?>> entry : RECIPE_TYPES.entrySet()) {
            Registry.register(Registry.RECIPE_TYPE, entry.getKey(), entry.getValue());
        }
    }


}

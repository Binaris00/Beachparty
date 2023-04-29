package net.satisfyu.beachparty.datagen;

import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BeachpartyAdvancementRecipeGenerator {

    public static String FOLDER = "/Users/marco/git/Beachparty/src/main/resources/data/beachparty";

    public static void main(String[] args) {
        List<String> putRecipesHere = List.of(

                "palm_planks/lounge_chair",
                "palm_planks/chair",
                "palm_planks/table",
                "palm_planks/cabinet",
                "palm_planks/beach_chair",
                "palm_planks/tiki_chair",
                "palm_planks/palm_slab",
                "palm_planks/palm_stairs",
                "palm_planks/palm_door",
                "palm_planks/palm_trapdoor",
                "palm_planks/palm_fence",
                "palm_planks/palm_fence_gate",
                "palm_planks/palm_button",
                "palm_planks/palm_pressure_plate",
                "palm_planks/palm_sign",
                "minecraft:bamboo/palm_torch",
                "minecraft:bamboo/palm_tall_torch",
                "minecraft:redstone/radio",
                "minecraft:iron_ingot/mini_fridge",
                "minecraft:white_wool/beach_towel",
                "minecraft:dried_kelp/sand_bucket_block",
                "palm_log/palm_planks",
                "dry_bush/minecraft:stick",
                "dry_bush_tall/minecraft:stick",
                "minecraft:wheat/beach_hat",
                "minecraft:glass_pane/sunglasses",
                "minecraft:string/bikini",
                "minecraft:string:trunks",
                "minecraft:dried_kelp:crocs",
                "minecraft:dried_kelp:swim_wings",
                "minecraft:dried_kelp:rubber_ring_blue",
                "minecraft:dried_kelp:rubber_ring_pink",
                "minecraft:dried_kelp:rubber_ring_stripped",
                "minecraft:dried_kelp:pool_noodle_blue",
                "minecraft:dried_kelp:pool_noodle_pink",
                "minecraft:dried_kelp:pool_noodle_green",
                "minecraft:dried_kelp:pool_noodle_red",
                "raw_pelican:cooked_pelican",
                "raw_mussel_meat:cooked_mussel_meat"







        );

        for(String s : putRecipesHere){
            List<String> list1 = Arrays.stream(s.split("/")).toList();

            if(list1.size() < 2){
                System.out.println("False entry: " + s);
                continue;
            }

            write(list1.get(0), list1.get(1));
        }
    }


    public static void write(String condition, String recipe) {
        String fullRecipe = "beachparty:" + recipe;
        String fullCondition = "beachparty:" + condition;

        if(condition.contains(":")){
            fullCondition = condition;
            condition = Arrays.stream(condition.split(":")).toList().get(1);
        }

        if(recipe.contains(":")){
            fullRecipe = condition;
            recipe = Arrays.stream(recipe.split(":")).toList().get(1);
        }



        // Writing
        try (FileWriter fileWriter = new FileWriter(FOLDER + recipe + ".json"); JsonWriter jsonWriter = new JsonWriter(fileWriter)) {
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject()
                    .name("parent").value("minecraft:recipes/root")
                    .name("rewards").beginObject().name("recipes").beginArray().value(fullRecipe).endArray().endObject()
                    .name("criteria").beginObject().name("has_" + condition).beginObject().name("trigger").value("minecraft:inventory_changed").name("conditions").beginObject().name("items").beginArray().beginObject().name("items").beginArray().value(fullCondition).endArray().endObject().endArray().endObject().endObject().name("has_the_recipe").beginObject().name("trigger").value("minecraft:recipe_unlocked").name("conditions").beginObject().name("recipe").value(fullRecipe).endObject().endObject().endObject()
                    .name("requirements").beginArray().beginArray().value("has_" + condition).value("has_the_recipe").endArray().endArray()

                    .endObject();
        } catch (IOException e) {
            System.out.printf("[beachparty] Couldn't write recipe to " + FOLDER + recipe);
            e.printStackTrace();
        }
    }
}


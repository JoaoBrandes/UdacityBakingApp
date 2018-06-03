package com.luiz.joao.udacitybakingapp.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeJson {

    private static final String TAG = RecipeJson.class.getSimpleName();
    private static final String idKey = "id";
    private static final String recipeNameKey = "name";
    private static final String servingsKey = "servings";
    private static final String imageKey = "image";
    private static final String stepsKey = "steps";
    private static final String ingredientsKey = "ingredients";

    public static ArrayList<Recipe> getRecipesFromJSON(String JsonStr)
            throws JSONException {

        JSONArray allRecipeJson = new JSONArray(JsonStr);

        ArrayList<Recipe> allRecipes = new ArrayList<>();

        int recipeId, recipeServings;
        String recipeName , recipeImage;
        ArrayList<Ingredient> ingredientList;
        ArrayList<Step> stepList;
        JSONObject recipeJson;
        JSONArray ingredientsJson, stepsJson;

        for(int i = 0; i < allRecipeJson.length(); i++){
            recipeJson = allRecipeJson.getJSONObject(i);
            recipeName = recipeJson.getString(recipeNameKey);
            recipeId = recipeJson.getInt(idKey);
            recipeServings = recipeJson.getInt(servingsKey);
            recipeImage = recipeJson.getString(imageKey);

            ingredientsJson = recipeJson.getJSONArray(ingredientsKey);
            stepsJson = recipeJson.getJSONArray(stepsKey);
            ingredientList = getIngredientsFromJSON(ingredientsJson);
            stepList = getStepsFromJSON(stepsJson);

            Recipe recipe = new Recipe(recipeId, recipeName, ingredientList,
                    stepList, recipeServings, recipeImage);
            allRecipes.add(recipe);
        }

        return allRecipes;
    }

    private static ArrayList<Ingredient> getIngredientsFromJSON(JSONArray ingredientsArray)
            throws JSONException {

        final String ingredientDescriptionKey = "ingredient";
        String quantityKey = "quantity";
        String measureKey = "measure";

        int quantity;
        String measure;
        String ingredientDescription;
        JSONObject ingredientObject;
        ArrayList<Ingredient> ingredientList = new ArrayList<>();

        for(int i = 0; i < ingredientsArray.length(); i++){
            ingredientObject = ingredientsArray.getJSONObject(i);
            quantity = ingredientObject.getInt(quantityKey);
            measure = ingredientObject.getString(measureKey);
            ingredientDescription = ingredientObject.getString(ingredientDescriptionKey);

            Ingredient ingredient = new Ingredient(quantity, measure, ingredientDescription);
            ingredientList.add(ingredient);
        }

        return ingredientList;

    }

    private static ArrayList<Step> getStepsFromJSON(JSONArray stepsArray)
            throws JSONException {

        final String idKey = "id";
        String shortDescriptionKey = "shortDescription";
        String descriptionKey = "description";
        String videoUrlKey = "videoURL";
        String thumbnailUrlKey = "thumbnailURL";

        int id;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailUrl;
        JSONObject stepObject;
        ArrayList<Step> stepList = new ArrayList<>();

        for(int i = 0; i < stepsArray.length(); i++){
            stepObject = stepsArray.getJSONObject(i);
            id = stepObject.getInt(idKey);
            shortDescription = stepObject.getString(shortDescriptionKey);
            description = stepObject.getString(descriptionKey);
            videoUrl = stepObject.getString(videoUrlKey);
            thumbnailUrl = stepObject.getString(thumbnailUrlKey);

            Step step = new Step(id, shortDescription, description, videoUrl, thumbnailUrl);
            stepList.add(step);
        }

        return stepList;
    }

    public static boolean validationJson(JSONObject moviesJson) throws JSONException {
        final String OWM_MESSAGE_CODE = "page";

        int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);
        Log.v(TAG,"ErrorCode : " + errorCode);
        if(moviesJson.has(OWM_MESSAGE_CODE)){
            return true;
        }
        return false;
    }
}

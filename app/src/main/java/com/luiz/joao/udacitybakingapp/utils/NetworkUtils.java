package com.luiz.joao.udacitybakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String UrlBase = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static List<Recipe> getRecipesList() {
        List<Recipe> jsonAllRecipes = null;
        try {
            URL url = new URL(UrlBase);
            String jsonRecipesResponse = NetworkUtils.getResponseFromHttpURL(url);
            jsonAllRecipes = RecipeJson.getRecipesFromJSON(jsonRecipesResponse);
        } catch(Exception e) {
            Log.e(TAG, "getRecipesList: " , e);
        }

        return jsonAllRecipes;
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static void main(String[] args) {
        List<Recipe> recipesList = getRecipesList();
        for(Recipe r : recipesList) {
            Log.i(TAG, "main: " + r.getRecipeName());
        }
    }

}

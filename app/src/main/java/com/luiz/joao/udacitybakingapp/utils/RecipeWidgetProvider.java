package com.luiz.joao.udacitybakingapp.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.luiz.joao.udacitybakingapp.DetailRecipeActivity;
import com.luiz.joao.udacitybakingapp.R;

/**
 * Created by Joaoe on 02/06/2018.
 * */

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {
        RemoteViews rv;
        rv = getIngredientListRemoteView(context,recipe);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    private static RemoteViews getIngredientListRemoteView(Context context, Recipe recipe) {
        Intent intent = new Intent(context, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews recipeView = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        StringBuilder ingredientList = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredientList()) {
            ingredientList.append(ingredient.getQuantity()).append(" ").append(ingredient.getIngredient())
                    .append(" ").append(ingredient.getMeasure()).append("\n");
        }

        recipeView.setTextViewText(R.id.recipe_title_widget, recipe.getRecipeName());
        recipeView.setTextViewText(R.id.recipe_list_widget, ingredientList.toString());
        recipeView.setOnClickPendingIntent(R.id.recipe_list_widget, pendingIntent);

        return recipeView;
    }
}

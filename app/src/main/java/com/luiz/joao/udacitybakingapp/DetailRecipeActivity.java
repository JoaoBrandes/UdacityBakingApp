package com.luiz.joao.udacitybakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luiz.joao.udacitybakingapp.utils.Ingredient;
import com.luiz.joao.udacitybakingapp.utils.Recipe;
import com.luiz.joao.udacitybakingapp.utils.RecipeWidgetProvider;
import com.luiz.joao.udacitybakingapp.utils.Step;

import java.util.ArrayList;

/**
 * Created by Joaoe on 01/06/2018.
 */

public class DetailRecipeActivity extends AppCompatActivity {

    private static Recipe recipe;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("recipe");
        } else {
            Intent intentDetailItem = getIntent();
            if (intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
                recipe = intentDetailItem.getExtras().getParcelable("recipe");
            }
        }

        String ingredientMeasure, ingredientDescription, ingredientQuantity;
        String stepId, stepShortDescription;
        String recipeName = recipe.getRecipeName();
        setTitle(recipeName);
        ArrayList<Ingredient> ingredientList = recipe.getIngredientList();
        final ArrayList<Step> stepList = recipe.getStepList();

        LinearLayout ingredientsLayout = findViewById(R.id.ingredients_list);
        for (Ingredient ingredient : ingredientList) {
            ingredientDescription = ingredient.getIngredient();
            ingredientQuantity = String.valueOf(ingredient.getQuantity());
            ingredientMeasure = ingredient.getMeasure();

            View ingredientView = LayoutInflater.from(this).inflate(R.layout.number_list_ingredient, ingredientsLayout, false);
            TextView ingredientText = ingredientView.findViewById(R.id.ingredient_item);
            ingredientText.setText(ingredientDescription + " " + ingredientQuantity + " " + ingredientMeasure);

            ingredientsLayout.addView(ingredientView);
        }

        LinearLayout stepsLayout = findViewById(R.id.steps_list);
        for (final Step step : stepList) {
            stepId = String.valueOf(step.getId());
            stepShortDescription = step.getShortDescription();
            View stepView = LayoutInflater.from(this).inflate(R.layout.number_list_step, stepsLayout, false);
            TextView stepText = stepView.findViewById(R.id.step_item);
            stepText.setText(stepId + " - " + stepShortDescription);

            stepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destinationClass = StepActivity.class;
                    Intent intentMovie = new Intent(v.getContext(), destinationClass);
                    intentMovie.putExtra("step", stepList);
                    intentMovie.putExtra("position", step.getId());
                    v.getContext().startActivity(intentMovie);
                }
            });

            stepsLayout.addView(stepView);
        }

        updateWidget();
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_layout);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("recipe", recipe);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable("recipe");
    }

}

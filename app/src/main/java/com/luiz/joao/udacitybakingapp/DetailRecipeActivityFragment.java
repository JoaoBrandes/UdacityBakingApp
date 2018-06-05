package com.luiz.joao.udacitybakingapp;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luiz.joao.udacitybakingapp.utils.Ingredient;
import com.luiz.joao.udacitybakingapp.utils.Recipe;
import com.luiz.joao.udacitybakingapp.utils.RecipeWidgetProvider;
import com.luiz.joao.udacitybakingapp.utils.Step;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailRecipeActivityFragment extends Fragment {

    private static Recipe recipe;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnImageClickListener {
        void onStepSelected(int position, ArrayList<Step> stepList);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public DetailRecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_list_fragment, container, false);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("recipe");
        } else {
            Intent intentDetailItem = getActivity().getIntent();
            if (intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
                recipe = intentDetailItem.getExtras().getParcelable("recipe");
            }
        }

        String ingredientMeasure, ingredientDescription, ingredientQuantity;
        String stepId, stepShortDescription;
        String recipeName = recipe.getRecipeName();
        getActivity().setTitle(recipeName);
        ArrayList<Ingredient> ingredientList = recipe.getIngredientList();
        final ArrayList<Step> stepList = recipe.getStepList();

        LinearLayout ingredientsLayout = rootView.findViewById(R.id.ingredients_list);
        for (Ingredient ingredient : ingredientList) {
            ingredientDescription = ingredient.getIngredient();
            ingredientQuantity = String.valueOf(ingredient.getQuantity());
            ingredientMeasure = ingredient.getMeasure();

            View ingredientView = LayoutInflater.from(getContext()).inflate(R.layout.number_list_ingredient, ingredientsLayout, false);
            TextView ingredientText = ingredientView.findViewById(R.id.ingredient_item);
            ingredientText.setText(ingredientDescription + " " + ingredientQuantity + " " + ingredientMeasure);

            ingredientsLayout.addView(ingredientView);
        }

        LinearLayout stepsLayout = rootView.findViewById(R.id.steps_list);
        for (final Step step : stepList) {
            stepId = String.valueOf(step.getId());
            stepShortDescription = step.getShortDescription();
            View stepView = LayoutInflater.from(getContext()).inflate(R.layout.number_list_step, stepsLayout, false);
            TextView stepText = stepView.findViewById(R.id.step_item);
            stepText.setText(stepId + " - " + stepShortDescription);

            ImageView thumbnail = stepView.findViewById(R.id.step_thumbnail);
            if (step.getThumbnailUrl() != null && !step.getThumbnailUrl().isEmpty()) {
                try {
                    Picasso.with(getContext()).load(step.getThumbnailUrl()).into(thumbnail);
                }catch (Exception ex) {
                    thumbnail.setVisibility(View.GONE);
                }
            } else {
                thumbnail.setVisibility(View.GONE);
            }

            stepView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onStepSelected(step.getId(), stepList);
                }
            });

            stepsLayout.addView(stepView);
        }

        updateWidget();
        return rootView;
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), RecipeWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_layout);
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(getContext(), appWidgetManager, recipe, appWidgetIds);
    }
}

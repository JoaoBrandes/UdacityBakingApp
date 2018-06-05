package com.luiz.joao.udacitybakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickGridViewItem_OpensRecipeActivity() {

        onView(withId(R.id.recyclerview_recipes)).perform(
                RecyclerViewActions.scrollToHolder(
                        withHolderRecipeView(RECIPE_NAME)
                )
        );
    }

    public static Matcher<RecyclerView.ViewHolder> withHolderRecipeView(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeAdapterViewHolder>(RecipeAdapter.RecipeAdapterViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeAdapterViewHolder item) {
                TextView timeViewText = item.itemView.findViewById(R.id.recipe_name_text);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }

}
package com.luiz.joao.udacitybakingapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luiz.joao.udacitybakingapp.utils.NetworkUtils;
import com.luiz.joao.udacitybakingapp.utils.Recipe;

import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private static RecipeAdapter recipeAdapter;
    public static GridView gridView;
    private static List<Recipe> recipesList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private static Parcelable state;

    public MainActivityFragment() {
        if (recipesList == null || recipesList.size() == 0) {
            loadRecipesData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_recipes);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        recipeAdapter = new RecipeAdapter(getActivity(), recipesList);
        mRecyclerView.setAdapter(recipeAdapter);

        return rootView;
    }

    public static void loadRecipesData() {
        //showMoviesDataView();
        new FetchRecipeTask().execute();
    }

    public static class FetchRecipeTask extends AsyncTask<String, Void, List<Recipe>> {
        private final String TAG = FetchRecipeTask.class.getSimpleName();

        protected void onPostExecute(List<Recipe> recipesList) {
            recipeAdapter.setRecipeData(recipesList);
        }

        protected List<Recipe> doInBackground(String... params) {
            try {
                List<Recipe> recipes = NetworkUtils.getRecipesList();
                recipesList = recipes;
                return recipesList;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
}
package com.luiz.joao.udacitybakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luiz.joao.udacitybakingapp.utils.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter  extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private List<Recipe> recipeData;
    private Context context;

    public RecipeAdapter(@NonNull Context context, @NonNull List<Recipe> objects) {
        this.recipeData = objects;
        this.context = context;
    }

    public void setRecipeData(List<Recipe> recipeData) {
        this.recipeData = recipeData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.number_list_recipe, parent, false);
        view.setFocusable(true);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = recipeData.get(position);
        holder.nameView.setText(recipe.getRecipeName());
        if (recipe.getImage() != null && recipe.getImage().length() > 1) {
            Picasso.with(context).load(recipe.getImage()).into(holder.recipeImage);
        } else {
            Picasso.with(context).load("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2008/10/1/0/EK0509_Healthy-Breakfast-Sandwich.jpg.rend.hgtvcom.616.462.suffix/1428520669563.jpeg").into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        if (recipeData != null) {
            return recipeData.size();
        } else {
            return 0;
        }
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView recipeImage;
        final TextView nameView;
        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            nameView = itemView.findViewById(R.id.recipe_name_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipeData.get(adapterPosition);
            Class destinationClass = DetailRecipeActivity.class;
            Intent intentMovie = new Intent(v.getContext(),destinationClass);
            intentMovie.putExtra("recipe", recipe);
            v.getContext().startActivity(intentMovie);
        }
    }
}
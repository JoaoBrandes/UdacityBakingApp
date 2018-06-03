package com.luiz.joao.udacitybakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    private final int id;
    private final String recipeName;
    private final ArrayList ingredientList;
    private final ArrayList stepList;

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    private final int servings;
    private final String image;


    public Recipe(int id, String recipeName, ArrayList<Ingredient> ingredientList,
                  ArrayList<Step> stepList, int servings, String image) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredientList = ingredientList;
        this.stepList = stepList;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        recipeName = in.readString();
        servings = in.readInt();
        image = in.readString();

        ingredientList = in.readArrayList(Ingredient.class.getClassLoader());
        stepList = in.readArrayList(Step.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipeName);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeList(ingredientList);
        dest.writeList(stepList);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

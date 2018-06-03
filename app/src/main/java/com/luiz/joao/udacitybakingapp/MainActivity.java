package com.luiz.joao.udacitybakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.luiz.joao.udacitybakingapp.utils.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_frying_pan);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }


}

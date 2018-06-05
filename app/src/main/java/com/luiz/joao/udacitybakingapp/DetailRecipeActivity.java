package com.luiz.joao.udacitybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.luiz.joao.udacitybakingapp.utils.Recipe;
import com.luiz.joao.udacitybakingapp.utils.Step;

import java.util.ArrayList;

/**
 * Created by Joaoe on 01/06/2018.
 */

public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeActivityFragment.OnImageClickListener {

    private boolean mTwoPane = false;
    private int stepPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        if (findViewById(R.id.detail_step_fragment) != null) {
            mTwoPane = true;
            Intent intentDetailItem = getIntent();
            if (intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
                Recipe recipe = intentDetailItem.getExtras().getParcelable("recipe");
                StepActivityFragment newFragment = new StepActivityFragment();
                newFragment.setArguments(recipe.getStepList().get(stepPosition), 0);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_step_fragment, newFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("stepPosition", stepPosition);
    }

    @Override
    public void onStepSelected(int position, ArrayList<Step> stepList) {
        if (mTwoPane == false) {
            Class destinationClass = StepActivity.class;
            Intent intentMovie = new Intent(this, destinationClass);
            intentMovie.putExtra("step", stepList);
            intentMovie.putExtra("position", position);
            startActivity(intentMovie);
        } else {
            stepPosition = position;
            StepActivityFragment newFragment = new StepActivityFragment();
            newFragment.setArguments(stepList.get(position), position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_step_fragment, newFragment)
                    .commit();
        }
    }
}

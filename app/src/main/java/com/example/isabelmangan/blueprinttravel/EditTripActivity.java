package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EditTripActivity extends AppCompatActivity {

    private boolean isAttraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button addAttractionButton = findViewById(R.id.add_attraction_button);
        addAttractionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isAttraction = true;
                addLocationUpdateUI(isAttraction);
            }
        });

        final Button addRestaurantButton = findViewById(R.id.add_restaurant_button);
        addRestaurantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isAttraction = false;
                addLocationUpdateUI(isAttraction);
            }
        });

    }

    /**
     * Update UI to Add Attraction OR Add Restaurant Activity
     */
    public void addLocationUpdateUI(boolean isAttrac) {
        //TODO: insert Add Attraction / Restaurant Activities into new intents

        if (isAttrac) {
            //Intent intent = new Intent(this, INSERTattractionCLASSHERE.class);
            //startActivity(intent);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        } else {
            //Intent intent = new Intent(this, INSERTrestaurantCLASSHERE.class);
            //startActivity(intent);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        }
    }

}

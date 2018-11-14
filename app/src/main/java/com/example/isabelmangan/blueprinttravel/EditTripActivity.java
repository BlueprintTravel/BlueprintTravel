package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class EditTripActivity extends AppCompatActivity implements LocationsRecyclerViewAdapter.ItemClickListener{

    private boolean isAttraction;
    private LocationsRecyclerViewAdapter attractionsAdapter;
    private LocationsRecyclerViewAdapter restaurantsAdapter;

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

        // data to populate the RecyclerView with
        // TODO: change from colors & animals - populate with images and placeNames
        ArrayList<Integer> viewColors = new ArrayList<>();
        viewColors.add(Color.BLUE);
        viewColors.add(Color.YELLOW);
        viewColors.add(Color.MAGENTA);
        viewColors.add(Color.RED);
        viewColors.add(Color.BLACK);

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the Attractions RecyclerView
        RecyclerView attractionsRecyclerView = findViewById(R.id.attractions_list);
        LinearLayoutManager horizontalAttrLayoutManager
                = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        attractionsRecyclerView.setLayoutManager(horizontalAttrLayoutManager);
        attractionsAdapter = new LocationsRecyclerViewAdapter(this, viewColors, animalNames);
        attractionsAdapter.setClickListener(this);
        attractionsRecyclerView.setAdapter(attractionsAdapter);

        // set up the Restaurants RecyclerView
        RecyclerView restaurantsRecyclerView = findViewById(R.id.restaurants_list);
        LinearLayoutManager horizontalRestLayoutManager
                = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        restaurantsRecyclerView.setLayoutManager(horizontalRestLayoutManager);
        restaurantsAdapter = new LocationsRecyclerViewAdapter(this, viewColors, animalNames);
        restaurantsAdapter.setClickListener(this);
        restaurantsRecyclerView.setAdapter(restaurantsAdapter);

        final Button generateRouteButton = findViewById(R.id.generate_route_button);
        generateRouteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateRoute();
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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + attractionsAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * Generates the trip's route
     */
    public void generateRoute() {
        //TODO: database stuff to take lists of attractions & restaurants and generate the route

        Intent intent = new Intent(this, MapActivity.class);
        //TODO: putExtra AKA send info back to MapActivity UI: route on map, Create Trip button now Edit Trip, Name of Trip displayed

        //TODO: progress bar
        startActivity(intent);

    }

}

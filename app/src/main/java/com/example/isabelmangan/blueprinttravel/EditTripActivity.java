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
    //TODO: make public?
    private ArrayList<Integer> viewAttrImagesList = new ArrayList<>();
    private ArrayList<String> attractionNamesList = new ArrayList<>();
    private ArrayList<Integer> viewRestImagesList = new ArrayList<>(); //TODO
    private ArrayList<String> restaurantNamesList = new ArrayList<>(); //TODO
    private ArrayList<AddAttractionActivity.Attraction> attractions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
        AddAttractionActivity.Attraction attraction = new AddAttractionActivity.Attraction();
        if (attraction.getPlaceID() != null) {

        }
         **/

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
        // TODO: delete colors & animals lol - populate with images and placeNames in better location
        viewAttrImagesList.add(Color.BLUE);
        viewAttrImagesList.add(Color.YELLOW);
        viewAttrImagesList.add(Color.MAGENTA);
        viewAttrImagesList.add(Color.RED);
        viewAttrImagesList.add(Color.BLACK);

        attractionNamesList.add("Horse");
        attractionNamesList.add("Cow");
        attractionNamesList.add("Camel");
        attractionNamesList.add("Sheep");
        attractionNamesList.add("Goat");

        //TODO: format better
        // set up the Attractions RecyclerView
        RecyclerView attractionsRecyclerView = findViewById(R.id.attractions_list);
        LinearLayoutManager horizontalAttrLayoutManager
                = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        attractionsRecyclerView.setLayoutManager(horizontalAttrLayoutManager);
        attractionsAdapter = new LocationsRecyclerViewAdapter(this, viewAttrImagesList, attractionNamesList);
        attractionsAdapter.setClickListener(this);
        attractionsRecyclerView.setAdapter(attractionsAdapter);

        // set up the Restaurants RecyclerView
        RecyclerView restaurantsRecyclerView = findViewById(R.id.restaurants_list);
        LinearLayoutManager horizontalRestLayoutManager
                = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        restaurantsRecyclerView.setLayoutManager(horizontalRestLayoutManager);
        restaurantsAdapter = new LocationsRecyclerViewAdapter(this, viewAttrImagesList, attractionNamesList);
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
            Intent intent = new Intent(this, AddAttractionActivity.class);
            startActivity(intent);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        } else {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            startActivity(intent);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + attractionsAdapter.getItem(position)
                + " on item position " + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * Generates the trip's route
     */
    public void generateRoute() {
        //TODO: database stuff to take lists of attractions & restaurants and generate the route

        Intent intent = new Intent(this, RouteMapActivity.class);
        //TODO: putExtra AKA send info back to MapActivity UI: route on map, Create Trip button now Edit Trip, Name of Trip displayed

        //TODO: progress bar
        startActivity(intent);

    }

}

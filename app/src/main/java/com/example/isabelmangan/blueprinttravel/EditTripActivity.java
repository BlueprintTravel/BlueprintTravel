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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditTripActivity extends AppCompatActivity implements LocationsRecyclerViewAdapter.ItemClickListener{

    private boolean isAttraction;
    private LocationsRecyclerViewAdapter attractionsAdapter;
    private LocationsRecyclerViewAdapter restaurantsAdapter;
    //TODO: make public?
    private ArrayList<Integer> viewAttrImagesList = new ArrayList<>();
    private ArrayList<String> attractionNamesList = new ArrayList<>();
    private ArrayList<Integer> viewRestImagesList = new ArrayList<>(); //TODO
    private ArrayList<String> restaurantNamesList = new ArrayList<>(); //TODO
    private ArrayList<Attraction> attractions = new ArrayList<>();


    String userID;
    String location;
    String tripName;
    LatLng latlng;
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
        FirebaseUser currUser = FirebaseHandler.getCurrentlySignedInUser();
        userID = currUser.getUid();
        location= getIntent().getStringExtra("TRIP_LOCATION");
        tripName = getIntent().getStringExtra("TRIP_NAME");

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        latlng = bundle.getParcelable("TRIP_LATLNG");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            String placeID = data.getStringExtra("placeID");
            double placeLat = data.getDoubleExtra("placeLat", 0);
            double placeLng = data.getDoubleExtra("placeLng", 0);
            int duration = data.getIntExtra("duration", 0);

            String placeName = data.getStringExtra("placeName");

            LatLng placeLatLng = new LatLng(placeLat, placeLng);

            Attraction attraction =
                    new Attraction(placeLatLng, placeID, duration, placeName);
            attractions.add(attraction);

        }
    }

    /**
     * Update UI to Add Attraction OR Add Restaurant Activity
     */
    public void addLocationUpdateUI(boolean isAttrac) {
        //TODO: insert Add Attraction / Restaurant Activities into new intents

        if (isAttrac) {
            Intent intent = new Intent(this, AddAttractionActivity.class);
            startActivityForResult(intent, 1);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        } else {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            startActivityForResult(intent, 2);

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
        GeoPoint geoPoint = new GeoPoint(latlng.latitude, latlng.longitude);

        Map<String, Object> newTrip = new HashMap<>();
        newTrip.put("tripName", tripName);
        newTrip.put("locationName", location);
        newTrip.put("LocationLatLng", geoPoint);

        FirebaseHandler.addAttractions(attractions, newTrip);
        for (int i = 0; i < attractions.size(); i++) {
            Log.d("mytag", "attraction " + i + " is " + attractions.get(i).placeName
            + " placeID: " + attractions.get(i).placeID + " lat/long is " + attractions.get(i).placeLatLng
            + " duration is " + attractions.get(i).duration);
        }

        Bundle args = new Bundle();
        args.putParcelable("TRIP_LATLNG", latlng);

        Intent intent = new Intent(this, RouteMapActivity.class);
        intent.putExtra("TRIP_LOCATION", location);
        intent.putExtra("TRIP_NAME", tripName);
        intent.putExtra("bundle", args);


        //TODO: progress bar
        startActivity(intent);

    }

}

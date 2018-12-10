package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.PhotoRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.android.SphericalUtil;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.GeoPoint;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.maps.PlaceDetailsRequest.FieldMask.OPENING_HOURS;

public class EditTripActivity extends AppCompatActivity implements LocationsRecyclerViewAdapter.ItemClickListener{

    private boolean isAttraction;
    private LocationsRecyclerViewAdapter attractionsAdapter;
    private LocationsRecyclerViewAdapter restaurantsAdapter;
    private static final String TAG = "AutocompleteFragment";
    //TODO: make public?
    private ArrayList<Integer> viewAttrImagesList = new ArrayList<>();

    private ArrayList<Integer> viewRestImagesList = new ArrayList<>(); //TODO
    private ArrayList<String> restaurantNamesList = new ArrayList<>(); //TODO
    private ArrayList<Attraction> attractions = new ArrayList<>();

    final ArrayList<String> DbAttractionList = new ArrayList<>();


    String userID;
    String location;
    String tripName;
    //LatLng latlng;
    Attraction start;

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
        FirebaseHandler fbHandler = new FirebaseHandler();
        FirebaseUser currUser = fbHandler.getCurrentlySignedInUser();
        userID = currUser.getUid();
        tripName = getIntent().getStringExtra("TRIP_NAME");
        addLatLngFromDB();
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
        //first check if db has any locations- add those
        //then check if new attraction is returned from add attraction
        //probably just the last item in attractions- attractions.size()-1


        //attractionNamesList.add("Chicago");
        //attractionNamesList.add("New York");
        //attractionNamesList.add("Seattle");
        //attractionNamesList.add("Austin");
        //attractionNamesList.add("San Diego");

        //restaurantNamesList.add("Chipotle");

        //add names from DB

        viewAttrImagesList.add(Color.BLUE);
        viewAttrImagesList.add(Color.BLUE);
        viewAttrImagesList.add(Color.BLUE);
        viewAttrImagesList.add(Color.BLUE);

        addNamesFromDB();

        addRestaurantNamesFromDB();

        // set up the Restaurants RecyclerView
        RecyclerView restaurantsRecyclerView = findViewById(R.id.restaurants_list);
        LinearLayoutManager horizontalRestLayoutManager
                = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
        restaurantsRecyclerView.setLayoutManager(horizontalRestLayoutManager);
        restaurantsAdapter = new LocationsRecyclerViewAdapter(this, viewAttrImagesList, restaurantNamesList);
        restaurantsAdapter.setClickListener(this);
        restaurantsRecyclerView.setAdapter(restaurantsAdapter);

        final Button generateRouteButton = findViewById(R.id.generate_route_button);
        generateRouteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(start != null && DbAttractionList.size() > 0){
                    generateRoute();
                }else if(start == null && DbAttractionList.size() <= 0){
                    for(int i = 0; i < 3; i++){
                        Toast.makeText(getBaseContext(), "Cannot Generate Route with no information",
                                Toast.LENGTH_LONG).show();
                    }
                }else if(start != null){
                    for(int i = 0; i < 3; i++){
                        Toast.makeText(getBaseContext(), "Please add Attraction(s) to your trip.",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    for (int i = 0; i < 3; i++) {
                        Toast.makeText(getBaseContext(), "Please select a Starting Location for your trip.",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public void addLatLngFromDB() {
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.getLatLngFromDB(tripName, new LatLngCallback() {
            LatLngBounds bounds;
            @Override
            public void onCallback(ArrayList<LatLng> locationLatLng) {
                for (int i = 0; i < locationLatLng.size(); i++) {
                    bounds = toBounds(locationLatLng.get(i),12000);
                }
                PlaceAutocompleteFragment autocompleteFragment;
                autocompleteFragment = (PlaceAutocompleteFragment)
                        getFragmentManager().findFragmentById(R.id.starting_place_autocomplete_fragment);
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        Log.i(TAG, "Place: " + place.getName());//get place details here

                        // TODO: Save that as first location in route algorithm -- send to database
                        String placeID = place.getId();
                        LatLng placeLatLng = place.getLatLng();
                        String placeName = place.getName().toString();
                        Boolean placeIsReq = true;
                        int placeDuration = 0;
                        String placeTripName = tripName;

                        start = new Attraction(placeLatLng, placeID, placeDuration, placeName, placeTripName, placeIsReq);
                        FirebaseHandler fbHandler = new FirebaseHandler();
                        fbHandler.addStartingLocationToDB(start);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                    }
                });
                autocompleteFragment.setHint("Enter Starting Location of Trip");
                autocompleteFragment.setBoundsBias(bounds);

            }
        });
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    public void addNamesFromDB() {
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.getAttractionsForCurrentTrip(tripName, new AttractionsCallback() {

            @Override
            public void onCallback(ArrayList<Attraction> attr) {
                ArrayList<String> attractionNamesList = new ArrayList<>();
                for (int i = attr.size()-1; i >= 0; i--) {
                    DbAttractionList.add(attr.get(i).getPlaceName());
                    boolean alreadyInList = false;
                    for (int j = 0; j < attractionNamesList.size(); j++) {
                        if (attractionNamesList.get(j).equals(attr.get(i).getPlaceName())) {
                            alreadyInList = true;
                        }
                    }
                    if (!alreadyInList) {
                        attractionNamesList.add(attr.get(i).getPlaceName());
                    }

                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig") //TODO: Change to our own API KEY
                            .build();
                    PhotoRequest request = PlacesApi.photo(context, attr.get(i).getPlaceID());
                    request.photoReference(attr.get(i).getPlaceID());
                    Log.d("testing123", "request is " + request);
                    try {
                        ImageResult pho = request.await();
                        //viewAttrImagesList.add(pho);
                    } catch (Exception ex){

                    }

                    //viewAttrImagesList.add(request);



                }



                //All logic needs to happen here!
                RecyclerView attractionsRecyclerView = findViewById(R.id.attractions_list);
                LinearLayoutManager horizontalAttrLayoutManager
                        = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
                attractionsRecyclerView.setLayoutManager(horizontalAttrLayoutManager);
                attractionsAdapter = new LocationsRecyclerViewAdapter(EditTripActivity.this, viewAttrImagesList, attractionNamesList);
                attractionsAdapter.setClickListener(EditTripActivity.this);
                attractionsRecyclerView.setAdapter(attractionsAdapter);

                //add photos


            }
        });
        Log.d("attractions list", "addNamesFromDB size outside is" + DbAttractionList.size());

    }

    public void addRestaurantNamesFromDB() {
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.getRestaurantsForCurrentTrip(tripName, new RestaurantCallback() {

            @Override
            public void onCallback(ArrayList<Restaurant> restaurants) {
                ArrayList<String> restaurantNamesList = new ArrayList<>();
                for (int i = restaurants.size()-1; i >= 0; i--) {
                    DbAttractionList.add(restaurants.get(i).getPlaceName());
                    boolean alreadyInList = false;
                    for (int j = 0; j < restaurantNamesList.size(); j++) {
                        if (restaurantNamesList.get(j).equals(restaurants.get(i).getPlaceName())) {
                            alreadyInList = true;
                        }
                    }
                    if (!alreadyInList) {
                        restaurantNamesList.add(restaurants.get(i).getPlaceName());
                    }


                    //viewAttrImagesList.add(request);



                }
                RecyclerView restaurantsRecyclerView = findViewById(R.id.restaurants_list);
                LinearLayoutManager horizontalRestLayoutManager
                        = new LinearLayoutManager(EditTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
                restaurantsRecyclerView.setLayoutManager(horizontalRestLayoutManager);
                restaurantsAdapter = new LocationsRecyclerViewAdapter(EditTripActivity.this, viewAttrImagesList, restaurantNamesList);
                restaurantsAdapter.setClickListener(EditTripActivity.this);
                restaurantsRecyclerView.setAdapter(restaurantsAdapter);


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            String placeID = data.getStringExtra("placeID");
            double placeLat = data.getDoubleExtra("placeLat", 0);
            double placeLng = data.getDoubleExtra("placeLng", 0);
            boolean isRequired = data.getBooleanExtra("isRequired", false);
            int duration = data.getIntExtra("duration", 0);

            String placeName = data.getStringExtra("placeName");

            LatLng placeLatLng = new LatLng(placeLat, placeLng);

            Attraction attraction =
                    new Attraction(placeLatLng, placeID, duration, placeName, tripName, isRequired);
            attractions.add(attraction);


            FirebaseHandler fbHander = new FirebaseHandler();
            fbHander.addAttractions(attraction);


        }

        if (requestCode == 2) {
            String placeID = data.getStringExtra("placeID");
            double placeLat = data.getDoubleExtra("placeLat", 0);
            double placeLng = data.getDoubleExtra("placeLng", 0);
            String placeName = data.getStringExtra("placeName");
            LatLng placeLatLng = new LatLng(placeLat, placeLng);

            Restaurant restaurant = new Restaurant(placeLatLng, placeID, placeName, tripName);
            FirebaseHandler fbHander = new FirebaseHandler();
            fbHander.addRestaurants(restaurant);
        }

        addNamesFromDB();
        addRestaurantNamesFromDB();
    }

    /**
     * Update UI to Add Attraction OR Add Restaurant Activity
     */
    public void addLocationUpdateUI(boolean isAttrac) {
        //TODO: insert Add Attraction / Restaurant Activities into new intents

        if (isAttrac) {
            Intent intent = new Intent(this, AddAttractionActivity.class);
            intent.putExtra("TRIP_NAME", tripName);
            startActivityForResult(intent, 1);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        } else {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            intent.putExtra("TRIP_NAME", tripName);
            startActivityForResult(intent, 2);

            //Test to assure proper click
            Log.d("Is it an attraction? ", String.valueOf(isAttrac));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + attractionsAdapter.getItem(position)
                + " on item position " + position, Toast.LENGTH_SHORT);
        FirebaseHandler fbHandler =  new FirebaseHandler();
        Intent intent = new Intent(this, EditAttractionActivity.class);
        fbHandler.getAttractionsForCurrentTrip(tripName, new AttractionsCallback() {
            public void onCallback(ArrayList<Attraction> attr) {
                for(int i = 0; i < attr.size(); i++){
                    Log.d("DYBALA","this ATTRACTION" + attr.get(i).placeName);
                    Log.d("DYBALA","want this"+DbAttractionList.get(position));
                    if(attr.get(i).placeName.equals(DbAttractionList.get(position))){
                        Attraction thisAttraction = attr.get(i);
                        Log.d("DYBALA","FOUND ATTRACTION" + thisAttraction.placeName);
                        intent.putExtra("calling_method","onItemClick");
                        intent.putExtra("LAT_LNG",thisAttraction.getLatLng());
                        intent.putExtra("PLACE_ID",thisAttraction.getPlaceID());
                        intent.putExtra("DURATION",thisAttraction.getDuration());
                        intent.putExtra("PLACE_NAME",thisAttraction.getPlaceName());
                        intent.putExtra("TRIP_NAME",thisAttraction.getTripName());
                        intent.putExtra("REQ",thisAttraction.getIsReq());
                        intent.putExtra("TRIP_NAME", tripName);
                        break;
                    }
                }
                Log.d("DYBALA","START INTENT");
                startActivity(intent);
            }
        });
    }

    /**
     * Generates the trip's route
     */
    public void generateRoute() {



        //TODO: database stuff to take lists of attractions & restaurants and generate the route





        Intent intent = new Intent(this, RouteMapActivity.class);

        intent.putExtra("TRIP_NAME", tripName);

        //TODO: progress bar
        startActivity(intent);

    }

}

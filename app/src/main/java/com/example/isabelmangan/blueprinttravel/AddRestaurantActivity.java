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
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class AddRestaurantActivity extends AppCompatActivity {




    private static final String TAG = "MyRestaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Restaurant addRestaurant = new Restaurant();

        //Autocomplete to get the place
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                //update attraction object with placeid
                addRestaurant.placeID = place.getId();
                addRestaurant.placeLatLng = place.getLatLng();
                addRestaurant.placeName = place.getName().toString();

                //Test placeid is correct
                Log.d(TAG, "Place ID: " + addRestaurant.placeID);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Button mAddAttractionButton = (Button) findViewById(R.id.add_restaurant_button);
        mAddAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Send restaurant placeid to database
                if(addRestaurant.placeID != null){

                    double placeLat = addRestaurant.placeLatLng.latitude;
                    double placelng = addRestaurant.placeLatLng.longitude;

                    setResult(2, new Intent().putExtra("placeLat", placeLat)
                            .putExtra("placeID", addRestaurant.placeID)
                            .putExtra("placeName", addRestaurant.placeName)
                            .putExtra("placeLng", placelng));
                    finish();
  
                }else{
                    for(int i = 0; i < 3; i ++){
                        Toast.makeText(getBaseContext(), "Please enter a Restaurant.",
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }





}

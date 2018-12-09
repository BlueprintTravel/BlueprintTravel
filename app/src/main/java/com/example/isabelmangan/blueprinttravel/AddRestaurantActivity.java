package com.example.isabelmangan.blueprinttravel;

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

public class AddRestaurantActivity extends AppCompatActivity {


    public class Restaurant{
        String placeid;
    }

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
                addRestaurant.placeid = place.getId();

                //Test placeid is correct
                Log.d(TAG, "Place ID: " + addRestaurant.placeid);
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
                if(addRestaurant.placeid != null) {
                    //TODO: Send restaurant placeid to database

                    //TODO: send info back to EditTrip
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

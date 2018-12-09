package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class AddAttractionActivity extends AppCompatActivity {



    private static final String TAG = "MyAttraction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create a new attraction object
        final Attraction addAttraction = new Attraction();

        //Autocomplete to get the place
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
               Log.i(TAG, "Place: " + place.getName());
                //update attraction object with placeid
                addAttraction.placeID = place.getId();
                addAttraction.placeLatLng = place.getLatLng();
                addAttraction.placeName = place.getName().toString();


                //Test placeid is correct
                Log.d(TAG, "Place ID: " + addAttraction.placeID);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Initiate a Switch for required state
       final Switch simpleSwitch = (Switch) findViewById(R.id.attraction_required_toggle);
        simpleSwitch.setChecked(true);


       //Define number pickers
        final NumberPicker hourPicker = findViewById(R.id.hourPicker);
        final NumberPicker minPicker = findViewById(R.id.minPicker);

        //minPicker.setDisplayedValues(null);

        //set min and max for hour clock
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(5);


        //set min and max for min clock
        minPicker.setMinValue(0);
        minPicker.setMaxValue(59);
        //minPicker.setDisplayedValues(new String[]{"0", "15", "30", "45"});


        hourPicker.setValue(1); //set default hour
        minPicker.setValue(30); //set default min



        //Add Attraction Button
        Button mAddAttractionButton = (Button) findViewById(R.id.add_attraction_button);
        mAddAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addAttraction.placeID != null){
                    //TODO: add attraction to list in database
                    //Set Required bool from Switch isChecked
                    addAttraction.isReq = simpleSwitch.isChecked();

                    //Test switch status
                    Log.d(TAG, "Required status: " + addAttraction.isReq);

                    //TODO: DELETE THIS IF NUM PICKER WORKS
                    //Get duration hours and minutes
                    //int hour = simpleTimePicker.getCurrentHour();
                    //int min = simpleTimePicker.getCurrentMinute();

                    //Set duration hours and minutes
                    int hour = hourPicker.getValue();
                    int min = minPicker.getValue();

                    //Calculate minutes total & set to duration for addAttraction
                    int totalMin = hour*60 + min;
                    addAttraction.duration = totalMin;

                    //TESTING FOR DURATION TIMEPICKER
                    //Log.i(TAG, "HOUR: " + hour );
                    //Log.i(TAG, "MIN: " + min );


                    //Test duration result
                    Log.d(TAG, "Duration: " + addAttraction.duration);



                    //TODO: send info back to EditTrip
                    //Attraction attr = new Attraction(addAttraction.placeLatLng, addAttraction.placeID,
                    //  addAttraction.duration, addAttraction.placeName);


                    double placeLat = addAttraction.placeLatLng.latitude;
                    double placelng = addAttraction.placeLatLng.longitude;

                    setResult(1, new Intent().putExtra("placeLat", placeLat)
                            .putExtra("placeID", addAttraction.placeID)
                            .putExtra("duration", addAttraction.duration)
                            .putExtra("placeName", addAttraction.placeName)
                            .putExtra("placeLng", placelng)
                            .putExtra("isRequired", addAttraction.isReq));
                    finish();
                }else{
                    for(int i = 0; i < 3; i ++){
                        Toast.makeText(getBaseContext(), "Please enter an Attraction.",
                                Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

        Button mCancelAddAttrButton = (Button) findViewById(R.id.cancel_add_attr);
        mCancelAddAttrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditTripActivity.class);
                intent.putExtra("TRIP_NAME", getIntent().getStringExtra("TRIP_NAME"));
                startActivity(intent);

               /*setResult(-1);
               finish();*/
            }
        });

    }


}

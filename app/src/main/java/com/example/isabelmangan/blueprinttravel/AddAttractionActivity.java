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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class AddAttractionActivity extends AppCompatActivity {

    public class Attraction{
        String placeID;
        LatLng placeLatLng;
        Boolean isReq;
        int duration;

        public Attraction() {

        }
        public Attraction(LatLng placeLatLng, String placeID, int duration) {
            setLatLng(placeLatLng);
            setPlaceID(placeID);
            setDuration(duration);
        }
        public void setLatLng(LatLng latLng) {
            this.placeLatLng = latLng;
        }
        public void setPlaceID(String placeID) {
            this.placeID = placeID;
        }
        public void setDuration(int duration) {
            this.duration = duration;
        }
        public LatLng getLatLng() {
            return placeLatLng;
        }
        public String getPlaceID() {
            return placeID;
        }
        public int getDuration() {
            return duration;
        }
    }

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
        simpleSwitch.setChecked(false);

        //final RadioGroup rb = (RadioGroup) findViewById(R.id.radioGroup);

        final TimePicker simpleTimePicker=(TimePicker)findViewById(R.id.simpleTimePicker); // initiate a time picker
        // set the value for current hours
        simpleTimePicker.setIs24HourView(true); // set 24 hours mode for the time picker


        //Add Attraction Button
        Button mAddAttractionButton = (Button) findViewById(R.id.add_attraction_button);
        mAddAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TODO: add attraction to list in database
                //Set Required bool from Switch isChecked
                addAttraction.isReq = simpleSwitch.isChecked();

                //Test switch status
                Log.d(TAG, "Required status: " + addAttraction.isReq);

                //Get duration hours and minutes
                int hour = simpleTimePicker.getCurrentHour();
                int min = simpleTimePicker.getCurrentMinute();

                //TODO: Calculate minutes total & set to duration for addAttraction
                int totalMin = hour*60 + min;
                addAttraction.duration = totalMin;

                //TESTING FOR DURATION TIMEPICKER
                //Log.i(TAG, "HOUR: " + hour );
                //Log.i(TAG, "MIN: " + min );


                //Test duration result
                Log.d(TAG, "Duration: " + addAttraction.duration);

                //TODO: send database addAttraction object

                //TODO: send info back to EditTrip
                finish();

            }
        });

    }


}

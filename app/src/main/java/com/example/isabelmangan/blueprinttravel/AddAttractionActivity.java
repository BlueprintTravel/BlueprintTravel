package com.example.isabelmangan.blueprinttravel;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;


public class AddAttractionActivity extends AppCompatActivity {

    public class Attraction{
        String placeid;
        Boolean isReq;
        String duration;
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
                addAttraction.placeid = place.getId();

                //Test placeid is correct
                Log.d(TAG, "Place ID: " + addAttraction.placeid);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
               // Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Set Required bool
        //Set when add attraction button is clicked
        // initiate a Switch
        final Switch simpleSwitch = (Switch) findViewById(R.id.attraction_required_toggle);
        simpleSwitch.setChecked(false);
        // check current state of a Switch (true or false).
        //addAttraction.isReq = simpleSwitch.isChecked();
        //switch test
       //Log.d(TAG, "Required status: " + addAttraction.isReq);


        //Set Duration string
        /**public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.radio_pirates:
                    if (checked)
                        // Pirates are the best
                        break;
                case R.id.radio_ninjas:
                    if (checked)
                        // Ninjas rule
                        break;
            }
        } **/


        //add attraction button takes user to edit trip activity

         Button mAddAttractionButton = (Button) findViewById(R.id.add_attraction_button);
         mAddAttractionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        //TODO: add attraction to list in database
            //TODO: Set Required bool from Switch isChecked
            addAttraction.isReq = simpleSwitch.isChecked();

            //Change switch text color to red if switch == true
            /**if (addAttraction.isReq == true){
                simpleSwitch.setTextColor(Color.RED);
            }**/

            //Test switch status
            Log.d(TAG, "Required status: " + addAttraction.isReq);

            //TODO: Set duration from radio buttons


        //TODO: Call edit trip activity
        //finishActivity(key);???
        }
        });

    }


}

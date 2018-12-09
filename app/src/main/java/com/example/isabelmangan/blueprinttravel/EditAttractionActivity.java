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

public class EditAttractionActivity extends AppCompatActivity {


    private static final String TAG = "MyAttraction";
    Attraction addAttraction;
    String tripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DYBALA","in correct message");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attraction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create a new attraction object
        Intent intent = getIntent();
        tripName = intent.getStringExtra("TRIP_NAME");
        LatLng placeLatLng= intent.getExtras().getParcelable("LAT_LNG");
        String placeID=intent.getStringExtra("PLACE_ID");
        int duration=intent.getIntExtra("DURATION",30);
        String placeName=intent.getStringExtra("PLACE_NAME");
        String tripName=intent.getStringExtra("TRIP_NAME");
        boolean isReq=intent.getBooleanExtra("REQ",Boolean.TRUE);
        addAttraction = new Attraction(placeLatLng,placeID,duration,placeName,tripName,isReq);





        //Initiate a Switch for required state
        final Switch simpleSwitch = (Switch) findViewById(R.id.attraction_required_toggle);
        simpleSwitch.setChecked(isReq);


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


        int hour = duration / 60;
        int minute = duration % 60;
        hourPicker.setValue(hour); //set default hour
        minPicker.setValue(minute); //set default min



        //Add Attraction Button
        Button mEditAttractionButton = (Button) findViewById(R.id.edit_attraction_button);
        mEditAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    double placeLng = addAttraction.placeLatLng.longitude;
                    FirebaseHandler fb =  new FirebaseHandler();
                    fb.editAttractionsForCurrentTrip(tripName,addAttraction.placeName,addAttraction);//editAttractionsForCurrentTrip(final String tripName, String attractionName, Attraction attr)
//                    setResult(1, new Intent().putExtra("placeLat", placeLat)
//                            .putExtra("placeID", addAttraction.placeID)
//                            .putExtra("duration", addAttraction.duration)
//                            .putExtra("placeName", addAttraction.placeName)
//                            .putExtra("placeLng", placeLng)
//                            .putExtra("isRequired", addAttraction.isReq));
                    finish();


            }
        });

    }


}


package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

public class CreateTripActivityScreen2 extends AppCompatActivity {

    private AutoCompleteTextView mTripName;
    private String tripName;
    private static final String TAG = "tripNameSuccessful";

    String userID;
    String location;
    LatLng latlng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip_screen2);

        mTripName = findViewById(R.id.tripName);
        FirebaseHandler fbHandler = new FirebaseHandler();
        FirebaseUser currUser = fbHandler.getCurrentlySignedInUser();


        Button mNextButton = (Button) findViewById(R.id.next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripName = mTripName.getText().toString();

                if(validateInputs(tripName) == true){
                    //TODO: trip name to database
                    //FirebaseHandler.setUpFirestore();
                    FirebaseUser currUser = fbHandler.getCurrentlySignedInUser();
                    userID = currUser.getUid();
                    location= getIntent().getStringExtra("TRIP_LOCATION");

                    Bundle bundle = getIntent().getParcelableExtra("bundle");
                    latlng = bundle.getParcelable("TRIP_LATLNG");
                    Log.d(TAG, "location is " + location + " latlng is " + latlng + " currentuserID is " + userID + " trip name is " + tripName);
                    fbHandler.addTrip(tripName, location, latlng);
                    updatePage();
                }else{
                    finish();
                    startActivity(getIntent());
                }


            }
        });
    }


    public boolean validateInputs(String tripName){
        if (tripName.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),"Trip Name Required", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
            return false;
        }

        return true;
    }

    public void updatePage() {
        Log.d(TAG, "trip name: " + tripName);
        Bundle args = new Bundle();
        args.putParcelable("TRIP_LATLNG", latlng);

        Intent intent = new Intent (this, EditTripActivity.class);
        intent.putExtra("TRIP_LOCATION", location);
        intent.putExtra("TRIP_NAME", tripName);
        intent.putExtra("bundle", args);
        startActivity(intent);
    }
}

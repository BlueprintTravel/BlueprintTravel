package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

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
        final FirebaseHandler fbHandler = new FirebaseHandler();
        FirebaseUser currUser = fbHandler.getCurrentlySignedInUser();


        Button mNextButton = (Button) findViewById(R.id.next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripName = mTripName.getText().toString();
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
            }
        });
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

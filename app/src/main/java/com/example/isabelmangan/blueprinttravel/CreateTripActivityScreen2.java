package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

public class CreateTripActivityScreen2 extends AppCompatActivity {

    private AutoCompleteTextView mTripName;
    private String tripName;
    private static final String TAG = "tripNameSuccessful";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip_screen2);

        mTripName = findViewById(R.id.tripName);


        Button mNextButton = (Button) findViewById(R.id.next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripName = mTripName.getText().toString();
                //TODO: trip name to database
                updatePage();
            }
        });
    }

    public void updatePage() {
        Log.d(TAG, "trip name: " + tripName);
        Intent intent = new Intent (this, EditTripActivity.class);
        startActivity(intent);
    }
}

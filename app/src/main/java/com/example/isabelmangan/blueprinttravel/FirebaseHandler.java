package com.example.isabelmangan.blueprinttravel;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;


public class FirebaseHandler {
    private static FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    private static FirebaseFirestore db;

    public static void setUpFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Initializes Firebase Authentication
     * @return authentication key
     */
    public static FirebaseAuth getAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    /**
     * Gets currently signed in Firebase User
     * @return current user
     */
    public static FirebaseUser getCurrentlySignedInUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public static void addUser(String userID) {
        // Create a new user with a first and last name
        Map<String, Object> userid = new HashMap<>();
        userid.put("userID", userID);


        // Add a new document with a generated ID
        db.collection("users")
                .add(userid)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



    public static void addTrip(String userid, String tripName, String LocationName, LatLng LocationLatLng){

        Double tripLat = LocationLatLng.latitude;
        Double tripLng = LocationLatLng.longitude;

        GeoPoint geoPoint = new GeoPoint(tripLat, tripLng);

        // Create a new trip with a trip name
        Map<String, Object> newTrip = new HashMap<>();
        newTrip.put("tripName", tripName);
        newTrip.put("locationName", LocationName);
        newTrip.put("LocationLatLng", geoPoint);


        db.collection("users").document(userid).collection("trips");

        db.collection("users").document(userid)
                .collection("trips").add(newTrip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



    }




}

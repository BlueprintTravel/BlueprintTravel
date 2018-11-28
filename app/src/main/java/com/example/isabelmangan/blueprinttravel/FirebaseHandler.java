package com.example.isabelmangan.blueprinttravel;

import android.location.Location;
import android.os.AsyncTask;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


public class FirebaseHandler {
    private  FirebaseAuth mAuth;
    private  final String TAG = "EmailPassword";

    private  FirebaseFirestore db;
    private  String tripID;
    private  String userRef = "";


    public  void setUpFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Initializes Firebase Authentication
     * @return authentication key
     */
    public  FirebaseAuth getAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    /**
     * Gets currently signed in Firebase User
     * @return current user
     */
    public  FirebaseUser getCurrentlySignedInUser(){
        getAuth();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public  void addUser() {

        setUpFirestore();
        //String userID = getCurrentlySignedInUser().getUid();
        // Create a new user with a first and last name
        Map<String, Object> userID = new HashMap<>();
        userID.put("userID", getCurrentlySignedInUser().getUid());
        userID.put("userEmail", getCurrentlySignedInUser().getEmail());


        // Add a new document with a generated ID

        db.collection("users").add(userID)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        userRef = documentReference.getId();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }


    public  void getCurrentTrip(final String tripName, final Map<String, Object> newLocation) {
        setUpFirestore();
        db.collection("users")
                .whereEqualTo("userID", getCurrentlySignedInUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userRef = document.getId();
                                //Log.d(TAG, "------" + userRef);
                                Log.d(TAG, "userRef is " + userRef);
                                db.collection("users").document(userRef)
                                        .collection("trips")
                                        .whereEqualTo("tripName", tripName).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        tripID = document.getId();
                                                        //Log.d(TAG, "------" + userRef);
                                                        Log.d(TAG, "tripID is " + tripID);
                                                        db.collection("users").document(userRef).collection("trips")
                                                                .document(tripID).collection("locations").add(newLocation)
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
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public  void addAttractions(ArrayList<Attraction> attractions, Map<String, Object> trip){
        setUpFirestore();
        String userid = getCurrentlySignedInUser().getUid();
        //db.collection("users").document(userRef).collection("trips")
          //      .document(tripID).collection("locations");
        for (int i = 0; i < attractions.size(); i++) {
            GeoPoint geoPoint = new GeoPoint(attractions.get(i).placeLatLng.latitude, attractions.get(i).placeLatLng.longitude);
            Attraction currentPlace = attractions.get(i);
            Map<String, Object> newLocation = new HashMap<>();
            newLocation.put("locationName", currentPlace.placeName);
            newLocation.put("duration", currentPlace.duration);
            newLocation.put("placeID", currentPlace.placeID);
            newLocation.put("isRequired", currentPlace.isReq);
            newLocation.put("LatLng", geoPoint);

            String tripName = currentPlace.getTripName();

            getCurrentTrip(tripName, newLocation);





        }


    }


    public  void getCurrentUser(final Map<String, Object> newTrip) {

        setUpFirestore();
        db.collection("users")
                .whereEqualTo("userID", getCurrentlySignedInUser().getUid()) // <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userRef = document.getId();
                                //Log.d(TAG, "------" + userRef);
                                Log.d(TAG, "userRef is " + userRef);
                                db.collection("users").document(userRef).collection("trips")
                                        .add(newTrip)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                tripID = documentReference.getId();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public  void addTrip(String tripName, String LocationName, LatLng LocationLatLng) {

        setUpFirestore();

        Log.d(TAG, "current userRef: " + userRef);

        Double tripLat = LocationLatLng.latitude;
        Double tripLng = LocationLatLng.longitude;

        GeoPoint geoPoint = new GeoPoint(tripLat, tripLng);

        // Create a new trip with a trip name
        final Map<String, Object> newTrip = new HashMap<>();
        newTrip.put("tripName", tripName);
        newTrip.put("locationName", LocationName);
        newTrip.put("LocationLatLng", geoPoint);

        getCurrentUser(newTrip);




    }

    /**
    public static ArrayList<Attraction> getAttractionsFromDB () {
        final ArrayList<Attraction> attrList = null;
        db.collection("users").document(userRef).collection("trips").
                document(tripID).collection("locations").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "--here11");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       // String latlng = document.getData().get("Latlng").toString();

                        Log.d(TAG, "got here!");
                        LatLng Latlng = new LatLng(5,5);

                        String placeID = document.getData().get("placeID").toString();
                        Log.d(TAG, "placeID is " + placeID);
                        String durationString = document.getData().get("duration").toString();
                        int duration = Integer.parseInt(durationString);
                        Log.d(TAG, "duration is " + duration);
                        String locationName = document.getData().get("locationName").toString();
                        Log.d(TAG, "locationName is " + locationName);
                        Attraction attraction =
                                new Attraction(Latlng, placeID, duration, locationName);
                        attrList.add(attraction);

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        return attrList;
    }
     */


    public  void getAttractionNamesForCurrentTrip(final String tripName, AttractionNamesCallback callback) {
        setUpFirestore();
        final ArrayList<String> attrNameList = new ArrayList<>();



        db.collection("users")
                .whereEqualTo("userID", getCurrentlySignedInUser().getUid()) // <-- This line
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userRef = document.getId();
                                //Log.d(TAG, "------" + userRef);
                                Log.d(TAG, "userRef is " + userRef);

                                db.collection("users").document(userRef).collection("trips")
                                        .whereEqualTo("tripName", tripName)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        tripID = document.getId();
                                                        //Log.d(TAG, "------" + userRef);
                                                        Log.d(TAG, "tripID is " + tripID);

                                                        db.collection("users").document(userRef).collection("trips").
                                                                document(tripID).collection("locations").
                                                                get().
                                                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                String locationName = document.getData().get("locationName").toString();

                                                                                attrNameList.add(locationName);

                                                                                Log.d(TAG, "inside size is : " + attrNameList.size());

                                                                                Log.d(TAG,  "!!! attraction name !!!" + locationName);
                                                                                callback.onCallback(attrNameList);

                                                                            }

                                                                        } else {
                                                                            Log.w(TAG, "Error getting documents.", task.getException());
                                                                        }

                                                                    }

                                                                });

                                                    }
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



    public  ArrayList<String> getActualAttractionNames(String userRef, String tripID) {
        final ArrayList<String> attrNameList = new ArrayList<>();
        db.collection("users").document(userRef).collection("trips").
                document(tripID).collection("locations").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String locationName = document.getData().get("locationName").toString();

                                attrNameList.add(locationName);

                                Log.d(TAG,  "!!! attraction name !!!" + locationName);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return attrNameList;
    }

}
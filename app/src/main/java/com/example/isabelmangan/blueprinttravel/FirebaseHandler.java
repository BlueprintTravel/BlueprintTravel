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


    /**
     * Initializes Firestore db
     * sets the local db variaable when this class is instantiated
     */
    public void setUpFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Initializes Firebase Authentication
     * @return authentication key
     */
    public FirebaseAuth getAuth() {
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

    /**
     * Adds a user to the db
     * Adds their userID and their email
     */
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


    /**
     * Helper method called by addAttraction that figures out what the current trip in the DB
     * is to store the attractions in the right place. This method then should actually store
     * the attractions
     *
     * For testing- test AddAttractions() since this is just called by that
     * @param tripName the name of the trip the attraction should be added to
     * @param newLocation the map of the new attraction location to be added to the trip
     */
    private void getCurrentTrip(final String tripName, final Map<String, Object> newLocation) {
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

    /**
     * Adds attractions to a trip
     * Creates a map object of each attraction in the attractions list and then calls getCurrentTrip
     * on each attraction in order to actually add it to the right trip.
     * @param attraction the list of attractions (name, duration, placeID, isRequired, LatLng)

     */
    public  void addAttractions(Attraction attraction){
        setUpFirestore();
        String userid = getCurrentlySignedInUser().getUid();
        //db.collection("users").document(userRef).collection("trips")
          //      .document(tripID).collection("locations");

            GeoPoint geoPoint = new GeoPoint(attraction.placeLatLng.latitude, attraction.placeLatLng.longitude);
            Attraction currentPlace = attraction;
            Map<String, Object> newLocation = new HashMap<>();
            newLocation.put("locationName", currentPlace.placeName);
            newLocation.put("duration", currentPlace.duration);
            newLocation.put("placeID", currentPlace.placeID);
            newLocation.put("isRequired", currentPlace.isReq);
            newLocation.put("LatLng", geoPoint);


            String tripName = currentPlace.getTripName();

            getCurrentTrip(tripName, newLocation);



    }

    /**
     * Helper method called by addTrip that figures out what the current user in the DB
     * is to store the trip in the right place. This method then should actually store
     * the trip
     *
     *  For testing- test addTrip() since this is just called by that
     * @param newTrip the map of the new trip that should be added to the user- newTrip holds
     *                trip name, location, and latlng
     */

    private void getCurrentUser(final Map<String, Object> newTrip) {

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

    /**
     * adds a new trip in the db
     * @param tripName the name of the trip to store
     * @param LocationName the location of the trip that should be stored
     * @param LocationLatLng the latlng location of the trip that should be stored
     */
    public void addTrip(String tripName, String LocationName, LatLng LocationLatLng) {

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


    /**
     * Gets the list of attraction names for the list to be displayed in Edit Trip.
     * The names are sent to the interface when they are fetched and then are retrieved from the
     * interface in EditTripActivity
     *
     * @param tripName the name of the trip that locations are shown for
     * @param callback the interface to send the location names
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


                                                                            }
                                                                            callback.onCallback(attrNameList);

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

    public  void getTripNamesForCurrentUser(TripNamesCallback callback) {
        setUpFirestore();
        final ArrayList<String> tripNamesList = new ArrayList<>();

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

                                db.collection("users").document(userRef).collection("trips")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        String tripName = document.getData().get("tripName").toString();
                                                        tripNamesList.add(tripName);
                                                    }
                                                    callback.onCallback(tripNamesList);
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


    /**
     * Gets the list of attractions for a specific trip in the database- for use in routemapActivity
     *
     * @param tripName the name of the trip that locations are shown for
     * @param callback the interface to send the location names
     */
    public void getAttractionsForCurrentTrip(final String tripName, AttractionsCallback callback) {
        setUpFirestore();
        final ArrayList<Attraction> attrList = new ArrayList<>();



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

                                                                                String placeName = document.getData().get("locationName").toString();
                                                                                //LatLng placeLatLng = document.getData().get("LatLng").toString();

                                                                                GeoPoint gp = document.getGeoPoint("LatLng");
                                                                                LatLng placeLatLng = new LatLng(gp.getLatitude(),gp.getLongitude());

                                                                                String placeID = document.getData().get("placeID").toString();
                                                                                String durationString = document.getData().get("duration").toString();
                                                                                int duration = Integer.parseInt(durationString);

                                                                                Boolean isReq = document.getBoolean("isRequired");

                                                                                Attraction attr = new Attraction(placeLatLng, placeID, duration, placeName,  tripName, isReq);

                                                                                attrList.add(attr);

                                                                                Log.d(TAG, "inside size is : " + attrList.size());



                                                                            }
                                                                            callback.onCallback(attrList);

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


    /**
     * Ignore this method for now I'm just using it for reference and then I will delete :)
     * @param userRef
     * @param tripID
     * @return
     */
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

    public void addStartingLocationToDB(Attraction start) {
        setUpFirestore();
        String userid = getCurrentlySignedInUser().getUid();
        //db.collection("users").document(userRef).collection("trips")
        //      .document(tripID).collection("locations");
        GeoPoint geoPoint = new GeoPoint(start.placeLatLng.latitude, start.placeLatLng.longitude);
        Attraction currentPlace = start;
        Map<String, Object> newLocation = new HashMap<>();
        newLocation.put("locationName", currentPlace.placeName);
        newLocation.put("duration", currentPlace.duration);
        newLocation.put("placeID", currentPlace.placeID);
        newLocation.put("isRequired", currentPlace.isReq);
        newLocation.put("LatLng", geoPoint);


        String tripName = currentPlace.getTripName();

        getCurrentTripStart(tripName, newLocation);
    }

    private void getCurrentTripStart(final String tripName, final Map<String, Object> newLocation) {
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
                                                        //check if has starting location, if so, get doc ref for it and set newLocation. If not, use document().set(newLocation)
                                                        String startLocationRef = db.collection("users").document(userRef)
                                                                .collection("trips").document(tripID).collection("startingLocation").getId();
                                                        if (!startLocationRef.equals("")) {
                                                            db.collection("users").document(userRef).collection("trips")
                                                                    .document(tripID).collection("startingLocation").document(startLocationRef).set(newLocation);
                                                        } else {
                                                            db.collection("users").document(userRef).collection("trips")
                                                                    .document(tripID).collection("startingLocation").add(newLocation);
                                                        }

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

    public void getStarLocationForCurrentTrip(String tripName, StartLocationCallback callback) {
        setUpFirestore();
        final ArrayList<Attraction> attrList = new ArrayList<>();



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
                                                                document(tripID).collection("startingLocation").
                                                                get().
                                                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                String placeName = document.getData().get("locationName").toString();
                                                                                //LatLng placeLatLng = document.getData().get("LatLng").toString();

                                                                                GeoPoint gp = document.getGeoPoint("LatLng");
                                                                                LatLng placeLatLng = new LatLng(gp.getLatitude(),gp.getLongitude());

                                                                                String placeID = document.getData().get("placeID").toString();
                                                                                String durationString = document.getData().get("duration").toString();
                                                                                int duration = Integer.parseInt(durationString);

                                                                                Boolean isReq = document.getBoolean("isRequired");

                                                                                Attraction attr = new Attraction(placeLatLng, placeID, duration, placeName,  tripName, isReq);

                                                                                attrList.add(attr);

                                                                                Log.d(TAG, "inside size is : " + attrList.size());

                                                                                callback.onCallback(attrList);

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

    /**
     * Edit Attraction in DB
     */
    public void editAttractionsForCurrentTrip(final String tripName, String attractionName, Attraction attr) {
        setUpFirestore();
        final ArrayList<Attraction> attrList = new ArrayList<>();

        GeoPoint geoPoint = new GeoPoint(attr.placeLatLng.latitude, attr.placeLatLng.longitude);
        Map<String, Object> EditedAttraction = new HashMap<>();
        EditedAttraction.put("locationName", attr.placeName);
        EditedAttraction.put("duration", attr.duration);
        EditedAttraction.put("placeID", attr.placeID);
        EditedAttraction.put("isRequired", attr.isReq);
        EditedAttraction.put("LatLng", geoPoint);


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
                                                        db.collection("users").document(userRef).collection("trips")
                                                                .document(tripID).collection("locations")
                                                                .whereEqualTo("locationName", attractionName)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            String locationId = document.getId();
                                                                            editAttraction(userRef, tripID, locationId, EditedAttraction );
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

    public void editAttraction(String userRef, String tripId, String locationId, Map<String, Object> EditedAttraction) {
        setUpFirestore();
        db.collection("users").document(userRef).collection("trips").document(tripId)
                .collection("locations").document(locationId).update(EditedAttraction);
    }

    /**
     * Logs the user out of firebase
     */
    public void logout() {
        getAuth();
        mAuth.signOut();
    }

}
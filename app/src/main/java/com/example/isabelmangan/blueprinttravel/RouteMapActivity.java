package com.example.isabelmangan.blueprinttravel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.OpeningHours;
import com.google.maps.model.PlaceDetails;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.maps.PlaceDetailsRequest.FieldMask.OPENING_HOURS;

public class RouteMapActivity extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "UpdateUI";

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;

    private static String location;
    private static String tripName;
    private static LatLng latlng;
    ActionBar actionbar;
    final ArrayList<Attraction> DbAttractionList = new ArrayList<>();

    static List<String> getplaces = new ArrayList();
    List<LatLng> latLngs = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Customized Toolbar for menu button support
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //actionbar.setTitle(tripName); //fix

        //Nav Drawer AKA Sidebar
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        return navigationDrawerHandler(menuItem);
                    }
                });

        Button mEditTripButton = (Button) findViewById(R.id.edit_trip_button);
        mEditTripButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUIToEditTrip();
            }
        });

        location= getIntent().getStringExtra("TRIP_LOCATION");
        tripName = getIntent().getStringExtra("TRIP_NAME");

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        latlng = bundle.getParcelable("TRIP_LATLNG");

        int attractionSize = getIntent().getIntExtra("ATTRACTION_LIST_SIZE", 0);
        Log.d(TAG, "attraction size: " + attractionSize);

        Bundle bundle2 = getIntent().getParcelableExtra("bundle2");
        for (int i = 0; i < attractionSize; i++) {
            LatLng latlongitude = bundle2.getParcelable("LOC_LATLNG" + i);
            latLngs.add(latlongitude);
            Log.d(TAG, "latlng: " + latlongitude);

            String Latlng = latlongitude.latitude + "," + latlongitude.longitude;
            Log.d(TAG, "------hereee------" + Latlng);
            Log.d(TAG, "string latlng: " + Latlng);
            getplaces.add(Latlng);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles Clicks in the Navigation Drawer AKA Sidebar
     */
    public boolean navigationDrawerHandler(MenuItem item) {
        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        drawerLayout.closeDrawers();

        // Handle action bar actions click -- swap UI fragments
        switch (item.getItemId()) {
            case R.id.nav_myTrips:
                //TODO: go to myTrips activity/fragment
                return true;
            case R.id.nav_favorites:
                //TODO: go to favorites activity/fragment
                return true;
            case R.id.nav_homepage:
                Intent intent = new Intent (this, MapActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                logoutUser();
                return true;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        //Define list to get all latlng for the route


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig") //TODO: Change to our own API KEY
                .build();


        List<LatLng> path = new ArrayList();
        List<String> myplaces = getplaces;

        addPlacesFromDB();


        for (int i = 0; i < latLngs.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(latLngs.get(i)));
        }



        for(int p = 0; p < myplaces.size()-1; p++) {
            DirectionsApiRequest req = DirectionsApi.getDirections(context, myplaces.get(p), myplaces.get(p+1));
            try {
                DirectionsResult res = req.await();

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs !=null) {
                        for(int i=0; i<route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j=0; j<leg.steps.length;j++){
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length >0) {
                                        for (int k=0; k<step.steps.length;k++){
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch(Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        //TODO: replace capitol with the first myplaces element
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 6));
    }

    public void addPlacesFromDB() {
        FirebaseHandler fbHandler = new FirebaseHandler();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig") //TODO: Change to our own API KEY
                .build();
        fbHandler.getAttractionsForCurrentTrip(tripName, new AttractionsCallback() {
                    public void onCallback(ArrayList<Attraction> attr) {
                        ArrayList<Attraction> attrList = new ArrayList<>();

                        for (int i = attr.size()-1; i >= 0; i--) {
                            DbAttractionList.add(attr.get(i));
                            boolean alreadyInList = false;
                            for (int j = 0; j < attrList.size(); j++) {
                                if (attrList.get(j).getPlaceName().equals(attr.get(i).getPlaceName())) {
                                    alreadyInList = true;
                                }
                            }
                            if (!alreadyInList) {
                                attrList.add(attr.get(i));
                            }

                        }
                        int size = attrList.size();
                        int[] open = new int[size];
                        int[] close = new int[size];
                        int[][] walking_time = new int[size][size];
                        int[] time_spent = new int[size];

                        //TODO: all logic needs to happen here!!
                        //attrList holds a list of attractions for the current trip
                        for (int i = 0; i < attrList.size(); i++) {
                            Log.d("emailpassword- routeMap", "attrList number " + i + " is :" + attrList.get(i).getPlaceName());
                        }

                        actionbar.setTitle(attrList.get(0).getTripName());
                        int curr = 540;
                        for (int i = 0; i < attrList.size(); i++) {
                            String placeId = attrList.get(i).getPlaceID();
                            int duration = attrList.get(i).getDuration();
                            time_spent[i] = duration;
                            int openTime = 0;
                            int closeTime = 24*60;
                            PlaceDetailsRequest request = PlacesApi.placeDetails(context, placeId).fields(OPENING_HOURS);
                            try {
                                Log.d("emailpassword", "here!");
                                PlaceDetails res = request.await();
                                Log.d("emailpassword", "this pn " + res.formattedPhoneNumber);
                                String[] openingHours = res.openingHours.weekdayText;
                                Log.d("emailpassword", "this is: " + openingHours.length);
                                int cat = 0;
                                int cat2 = 0;
                                Calendar calendar = Calendar.getInstance();
                                int day = calendar.get(Calendar.DAY_OF_WEEK);

                                //SUNDAY = 1
                                //MONDAY = 2
                                //TUESDAY = 3
                                //WEDNESDAY = 4
                                //THURSDAY = 5
                                //FRIDAY = 6
                                //SATURDAY = 7

                                if ((day == 1) || (day == 2) || (day == 6)) {
                                    cat = 7;
                                    cat2 = 22;
                                }
                                if (day == 3) {
                                    cat = 8;
                                    cat2 = 24;
                                }
                                if (day == 4) {
                                    cat = 10;
                                    cat2 = 28;
                                }
                                if ((day == 5) || (day == 7)) {
                                    cat = 9;
                                    cat2 = 26;
                                }

                                OpeningHours.Period[] per = res.openingHours.periods;
                                for (int j = 0; j < openingHours.length; j++) {
                                    Log.d("emailpassword", "opening hour " + openingHours[j]);
                                    Log.d("emailpassword", "per opening hour " + per[j].toString());
                                }
                                String startHourString = per[day-1].toString().substring(cat, cat+2);
                                int startHour = Integer.parseInt(startHourString);

                                String startMinString = per[day-1].toString().substring(cat+3, cat+5);
                                int startMin = Integer.parseInt(startMinString);

                                String endHourString = per[day-1].toString().substring(cat2, cat2+2);
                                int endHour = Integer.parseInt(endHourString);

                                String endMinString = per[day-1].toString().substring(cat2+3, cat2+5);
                                int endMin = Integer.parseInt(endMinString);
                                if (!((startHour == 0) && (startMin == 0))) {
                                    openTime = (startHour*60) + startMin;
                                }
                                if (!((endHour == 0) && (endHour == 0))) {
                                    closeTime = (endHour*60) + endMin;
                                }


                                Log.d("emailpassword", "per opening hour " + per[day-1].toString().substring(cat, cat+2));
                                Log.d("emailpassword", "per opening min " + per[day-1].toString().substring(cat+3, cat+5));

                                Log.d("emailpassword", "per closing hour " + per[day-1].toString().substring(cat2, cat2+2));
                                Log.d("emailpassword", "per closing min " + per[day-1].toString().substring(cat2+3, cat2+5));

                                open[i] = openTime;
                                close[i] = closeTime;


                                //open time, close time, time spent, walking time

                            } catch (Exception ex) {

                            }


                        }
                        String[] origins = new String[attrList.size()];
                        String[] destination = new String[attrList.size()];
                        for (int i = 0; i < attrList.size(); i++) {
                            origins[i] = attrList.get(i).getPlaceID();
                            destination[i] = attrList.get(i).getPlaceID();
                        }

                        DistanceMatrixApiRequest matrix = DistanceMatrixApi.getDistanceMatrix(context, origins, destination);
                        try {
                            //IM STILL WORKING ON THIS
                        } catch (Exception ex) {
                            //IM STILL WORKING ON THIS
                        }


                        for (int i = 0; i < open.length; i++) {
                            if ((open[i] ==0) && close[i] == 0) {
                                close[i] = 24*60;
                            }
                        }
                        for (int i = 0; i < open.length; i++) {
                            Log.d("emailpassword", "open " + i + " : " + open[i]);
                        }
                        for (int i = 0; i < close.length; i++) {
                            Log.d("emailpassword", "close " + i + " : " + close[i]);
                        }
                        for (int i = 0; i < time_spent.length; i++) {
                            Log.d("emailpassword", "time spent at " + i + " : " + time_spent[i]);
                        }



                        //TODO: call the algorithm










                    }
        });
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Logs out the user.
     * */
    private void logoutUser() {

        //TODO: logout functionality connecting to database
        //session.setLogin(false);
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.logout();
        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(RouteMapActivity.this, LoginActivity.class);
        Toast toast = Toast.makeText(getApplicationContext(),
                "You are now logged out.\nThank you for using Blueprint Travel.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        startActivity(intent);
        toast.show();
        finish();
    }

    public void changeUIToEditTrip() {
        Log.d(TAG, "Enter Edit Trip");
        Bundle args = new Bundle();
        args.putParcelable("TRIP_LATLNG", latlng);
        Intent intent = new Intent (this, EditTripActivity.class);
        intent.putExtra("TRIP_LOCATION", location);
        intent.putExtra("TRIP_NAME", tripName);
        intent.putExtra("bundle", args);

        startActivity(intent);
    }
}
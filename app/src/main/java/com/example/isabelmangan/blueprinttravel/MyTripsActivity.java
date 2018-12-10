package com.example.isabelmangan.blueprinttravel;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity implements MyTripsRecyclerViewAdapter.ItemClickListener {

    private DrawerLayout drawerLayout;
    MyTripsRecyclerViewAdapter adapter;

    ArrayList<String> tripDisplayNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);



        addNamesFromDB();

        //Customized Toolbar for menu button support
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("My Trips");

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
    }

    public void addNamesFromDB() {
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.getTripNamesForCurrentUser(new TripNamesCallback() {

            @Override
            public void onCallback(ArrayList<String> tripNames) {
                tripDisplayNames = tripNames;


                //All logic needs to happen here!
                RecyclerView recyclerView = findViewById(R.id.rvTrips);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyTripsActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new MyTripsRecyclerViewAdapter(MyTripsActivity.this, tripNames);
                adapter.setClickListener(MyTripsActivity.this);
                recyclerView.setAdapter(adapter);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);


            }
        });


    }


    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, RouteMapActivity.class);

        intent.putExtra("TRIP_NAME", adapter.getItem(position));

        startActivity(intent);
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
     * Logs out the user.
     * */
    private void logoutUser() {

        //TODO: logout functionality connecting to database
        //session.setLogin(false);
        FirebaseHandler fbHandler = new FirebaseHandler();
        fbHandler.logout();
        //db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        for(int i = 0; i < 3; i++){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You are now logged out.\nThank you for using Blueprint Travel.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            startActivity(intent);
            toast.show();
        }
        finish();
    }
}

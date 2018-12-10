package com.example.isabelmangan.blueprinttravel;

import android.annotation.TargetApi;

import java.util.ArrayList;

public interface RestaurantCallback {

        @TargetApi(24)
        void onCallback(ArrayList<Restaurant> restaurants);


}

package com.example.isabelmangan.blueprinttravel;

import android.annotation.TargetApi;

import java.util.ArrayList;

public interface TripNamesCallback {
    @TargetApi(24)
    void onCallback(ArrayList<String> tripNames);
}

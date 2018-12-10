package com.example.isabelmangan.blueprinttravel;

import android.annotation.TargetApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface LatLngCallback {
    @TargetApi(24)
    void onCallback(ArrayList<LatLng> locationLatLng);
}

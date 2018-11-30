package com.example.isabelmangan.blueprinttravel;

import android.annotation.TargetApi;

import java.util.ArrayList;

public interface StartLocationCallback {
    @TargetApi(24)
    void onCallback(ArrayList<Attraction> startLocation);
}

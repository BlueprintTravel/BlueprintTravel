package com.example.isabelmangan.blueprinttravel;

import android.annotation.TargetApi;
import android.util.Log;

import java.util.ArrayList;

public interface AttractionNamesCallback {
    @TargetApi(24)
    void onCallback(ArrayList<String> attractionNames);

}

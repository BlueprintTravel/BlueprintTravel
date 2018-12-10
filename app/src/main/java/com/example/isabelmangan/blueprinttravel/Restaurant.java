package com.example.isabelmangan.blueprinttravel;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    String placeID;
    LatLng placeLatLng;
    String placeName;
    String tripName;

    public Restaurant() {

    }
    public Restaurant(LatLng placeLatLng, String placeID, String placeName, String tripName) {
        setLatLng(placeLatLng);
        setPlaceID(placeID);
        setPlaceName(placeName);
        setTripName(tripName);
    }
    public void setLatLng(LatLng latLng) {
        this.placeLatLng = latLng;
    }
    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public LatLng getLatLng() {
        return placeLatLng;
    }
    public String getPlaceID() {
        return placeID;
    }
    public String getPlaceName() {
        return placeName;
    }
    public String getTripName() { return tripName; }
}

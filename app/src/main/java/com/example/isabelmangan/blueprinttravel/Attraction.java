package com.example.isabelmangan.blueprinttravel;

import com.google.android.gms.maps.model.LatLng;

public class Attraction {
    String placeID;
    LatLng placeLatLng;
    Boolean isReq;
    int duration;
    String placeName;
    String tripName;

    public Attraction() {

    }
    public Attraction(LatLng placeLatLng, String placeID, int duration, String placeName, String tripName, boolean isReq) {
        setLatLng(placeLatLng);
        setPlaceID(placeID);
        setDuration(duration);
        setPlaceName(placeName);
        setTripName(tripName);
        setIsReq(isReq);
    }
    public void setLatLng(LatLng latLng) {
        this.placeLatLng = latLng;
    }
    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public void setIsReq(Boolean isReq) { this.isReq = isReq; }
    public LatLng getLatLng() {
        return placeLatLng;
    }
    public String getPlaceID() {
        return placeID;
    }
    public String getPlaceName() {
        return placeName;
    }
    public int getDuration() {
        return duration;
    }
    public String getTripName() { return tripName; }
    public boolean getIsReq() { return isReq; }
}

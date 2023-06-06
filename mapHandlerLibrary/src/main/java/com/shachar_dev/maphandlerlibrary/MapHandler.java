package com.shachar_dev.maphandlerlibrary;

import androidx.appcompat.widget.SearchView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public interface MapHandler {
    Marker createMarker(LatLng latLng, String title, BitmapDescriptor icon);

    void animateMarker(Marker marker, LatLng latLng);

    void showCircleOnCurrentLocation(int radius, int strokeColor, int fillColor, int stroke);

    void showCircleOnLocation(LatLng latLng, int radius, int strokeColor, int fillColor, int stroke);

    void captureScreen(MapSnapshotCallback snapshotCallback);

    void clearMap();

    void setMarkerOnCurrentLocation();

    void removeMarker(Marker marker);

    void SearchLocation(SearchView searchView);
}

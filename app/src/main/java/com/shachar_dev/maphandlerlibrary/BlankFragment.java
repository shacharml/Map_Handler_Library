package com.shachar_dev.maphandlerlibrary;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class BlankFragment extends Fragment implements MapHandler {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public Marker createMarker(LatLng latLng, String title, BitmapDescriptor icon) {
        return null;
    }

    @Override
    public void animateMarker(Marker marker, LatLng latLng) {

    }

    @Override
    public void showCircleOnCurrentLocation(int radius, int strokeColor, int fillColor, int stroke) {

    }

    @Override
    public void showCircleOnLocation(LatLng latLng, int radius, int strokeColor, int fillColor, int stroke) {

    }

    @Override
    public void captureScreen(MapSnapshotCallback snapshotCallback) {

    }

    @Override
    public void clearMap() {

    }

    @Override
    public void setMarkerOnCurrentLocation() {

    }

    @Override
    public void removeMarker(Marker marker) {

    }

    @Override
    public void SearchLocation(SearchView searchView) {

    }
}
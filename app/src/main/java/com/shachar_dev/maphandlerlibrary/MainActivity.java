package com.shachar_dev.maphandlerlibrary;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class MainActivity extends AppCompatActivity implements MapUtils.OnMarkerClickListener, MapCallback {

    private MapHandler mapHandler;
    private SearchView idSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idSearchView = findViewById(R.id.idSearchView);
        initializeMapFragment(savedInstanceState);
    }

    private void initializeMapFragment(Bundle savedInstanceState) {
        mapHandler = new MapsFragmentFactory().createMapFragment(savedInstanceState, getSupportFragmentManager(), R.id.main_fragment);
    }

    @Override
    public void onMapInitialized() {
        mapHandler.createMarker(new LatLng(40.7128, -74.0060), "New York", BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mapHandler.showCircleOnCurrentLocation(300, Color.BLUE, Color.argb(20, 0, 0, 255), 2);
        mapHandler.SearchLocation(idSearchView);
    }

    @Override
    public void onCurrentLocationChange(CustomLocation currentLocation) {

    }

    @Override
    public void onMarkerClick(Marker marker) {
        Toast.makeText(this, "Marker Clicked - " + marker.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
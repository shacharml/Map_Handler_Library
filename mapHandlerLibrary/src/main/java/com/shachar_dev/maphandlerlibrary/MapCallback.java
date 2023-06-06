package com.shachar_dev.maphandlerlibrary;

public interface MapCallback {
    void onMapInitialized();

    void onCurrentLocationChange(CustomLocation currentLocation);
}

package com.shachar_dev.maphandlerlibrary;

import com.google.android.gms.maps.model.LatLng;

public class CustomLocation {
    private String name; //The title of the Marker on the Map
    private String address;
    private double longitude;
    private double latitude;
    private String locationImage;

    public CustomLocation() {
    }

    public CustomLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CustomLocation(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public CustomLocation(String name, String address, String locationImage, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.locationImage = locationImage;
    }

    public String getName() {
        return name;
    }

    public CustomLocation setName(String name) {
        this.name = name;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public CustomLocation setAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomLocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public CustomLocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public boolean equals(CustomLocation location) {
        return latitude == location.getLatitude() && longitude == location.getLongitude();
    }

    public String getLocationImage() {
        return locationImage;
    }

    public CustomLocation setLocationImage(String locationImage) {
        this.locationImage = locationImage;
        return this;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "CustomLocation{" +
                "name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}

package com.shachar_dev.maphandlerlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapUtils {

    private final GoogleMap map;

    static final float DEFAULT_ZOOM = 16f;

    public interface OnMarkerClickListener {
        void onMarkerClick(Marker marker);
    }

    @SuppressLint("PotentialBehaviorOverride")
    public MapUtils(GoogleMap map, Activity activity, OnMarkerClickListener onMarkerClickListener) {
        this.map = map;
        map.setOnMarkerClickListener(marker -> {
            onMarkerClickListener.onMarkerClick(marker);
            return true;
        });
    }

    public MapUtils(GoogleMap map) {
        this.map = map;
    }

    public Circle createCircle(LatLng currentLocation, int radius, int strokeColor, int fillColor, int stroke) {
        // Create a new CircleOptions object
        CircleOptions circleOptions = new CircleOptions().center(currentLocation) // Set the center of the circle to (0, 0) by default
                .radius(radius) // Set the radius of the circle to 1000 meters by default
                .strokeWidth(stroke) // Set the stroke width of the circle's outline to 2 pixels
                .strokeColor(strokeColor) // Set the stroke color of the circle's outline to blue
                .fillColor(fillColor); // Set the fill color of the circle to translucent blue
        // Add the circle to the map and save a reference to it
        Circle c = map.addCircle(circleOptions);
        c.setCenter(currentLocation);
        return c;
    }


    public Marker createMarker(LatLng latLng, String title, BitmapDescriptor icon) {
        return map.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
    }

    public void moveCamera(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void moveCameraWitZoom(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    public void animateMarker(final Marker marker, final LatLng finalPosition) {
        // Check if the start and final positions are the same
        if (marker.getPosition().equals(finalPosition)) {
            // If they are the same, move the marker to the final position and exit the method
            marker.setPosition(finalPosition);
            return;
        }

        // Get a new handler to run the animation
        final Handler handler = new Handler();
        final LatLng startPosition = marker.getPosition();

        final long duration = 5000;
        // Get the current time in milliseconds as the start time for the animation
        final long start = SystemClock.uptimeMillis();
        // Define an interpolator to determine the marker's position at each point in time
        final Interpolator interpolator = new LinearInterpolator();
        // Calculate the distance between the start and end positions in meters
        final float distance = (float) SphericalUtil.computeDistanceBetween(startPosition, finalPosition);

        // Define a new runnable to update the marker's position at each point in time
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Calculate the elapsed time in milliseconds since the animation started
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate the fraction of the duration that has elapsed so far
                float t = interpolator.getInterpolation((float) elapsed / duration);
                // Calculate the distance covered by the marker so far
                float distanceCovered = t * distance;
                // Calculate the new position of the marker using spherical interpolation
                LatLng newPosition = SphericalUtil.interpolate(startPosition, finalPosition, distanceCovered / distance);
                // Update the position of the marker to the new position
                marker.setPosition(newPosition);
                // If the marker has reached the final position, remove the runnable from the handler to stop the animation
                if (distanceCovered >= distance) {
                    handler.removeCallbacks(this);
                } else {
                    // Otherwise, continue to post the runnable to the handler with a delay of 16 milliseconds (equivalent to a frame rate of approximately 60 frames per second)
                    handler.postDelayed(this, 16);
                }
            }
        };
        // Post the runnable to the handler to start the animation
        handler.post(runnable);
    }

    public String locationToAddress(CustomLocation location, Activity activity) {
        // Convert latitude and longitude to address
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // get the first address line
                String addressLine = address.getAddressLine(0);
                Log.d("ptt", "Current address: " + addressLine);
                return addressLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMarkerLocation(Marker marker, CustomLocation location, Circle mCircle, boolean withZoom) {
        if (marker != null)
            marker.setPosition(location.toLatLng());
        if (mCircle != null)
            mCircle.setCenter(location.toLatLng());
        // Move the camera to the new location
        if (withZoom)
            moveCameraWitZoom(location.toLatLng());
        else
            moveCamera(location.toLatLng());
    }

}

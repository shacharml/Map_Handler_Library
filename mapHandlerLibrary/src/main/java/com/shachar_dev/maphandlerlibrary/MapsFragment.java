package com.shachar_dev.maphandlerlibrary;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback, MapHandler {


    //Map Fragment
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String RATIONAL = "CustomLocation permission is required for this feature.";
    public static final String MESSAGE = "Setting screen if user have permanently disable the permission by clicking Don't ask again checkbox.";
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private CustomLocation currentLocation;
    private CustomLocation SearchLocation;
    private Location previousLocation;
    private Marker currentLocationMarker;
    private Circle mCircle;
    private SupportMapFragment mapFragment;
    private boolean isFirstTime = true;
    //Utils and Helpers
    private PermissionsUtils permissionsUtils;
    private MapUtils mapUtils;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        currentLocation = new CustomLocation();
        SearchLocation = new CustomLocation();
        currentLocation.setName("Current Location");
        buildLocationCallback();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // init permissions utils
        permissionsUtils = new PermissionsUtils(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(permissionsUtils);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            // Call getMapAsync() to set the callback on the map
            mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        mapUtils = new MapUtils(map, requireActivity(), marker -> {
            if (requireActivity() instanceof MapUtils.OnMarkerClickListener)
                ((MapUtils.OnMarkerClickListener) requireActivity()).onMarkerClick(marker);
        });
        permissionsUtils.askForPermissions((AppCompatActivity) requireActivity(), () -> {
            map.setMyLocationEnabled(true);
            startLocationUpdates();
        }, LOCATION_PERMISSIONS, MESSAGE, RATIONAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (requireActivity() instanceof MapCallback) {
            ((MapCallback) requireActivity()).onMapInitialized();
        }
    }

    @Override
    public void showCircleOnCurrentLocation(int radius, int strokeColor, int fillColor, int stroke) {
        showCircleOnLocation(currentLocation.toLatLng(), radius, strokeColor, fillColor, stroke);
    }

    @Override
    public void showCircleOnLocation(LatLng latLng, int radius, int strokeColor, int fillColor, int stroke) {
        if (mCircle != null) {
            mCircle.setCenter(latLng);
        } else {
            mCircle = mapUtils.createCircle(latLng, radius, strokeColor, fillColor, stroke);
        }
    }

    @Override
    public void captureScreen(MapSnapshotCallback snapshotCallback) {
        if (map != null) map.snapshot(snapshotCallback::onSnapshotReady);
        else snapshotCallback.onSnapshotReady(null);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(5000).setMinUpdateIntervalMillis(1000).setPriority(PRIORITY_HIGH_ACCURACY).build();
        //Requests location updates with the given request and results delivered to the given callback on the specified Looper.
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            /**
             Called when a new LocationResult is available.
             The locations within the location result will generally be as fresh
             as possible given the parameters of the associated LocationRequest
             and the state of the device
             */
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Get the new location
                Location location = locationResult.getLastLocation();
                if (location == null) return;
                // Compare with the previous location
                if (previousLocation != null && location.distanceTo(previousLocation) < 10)
                    // Location has not changed significantly, do nothing
                    return;

                // Location has changed significantly, update the map
                previousLocation = location;
                currentLocation.setLatitude(location.getLatitude()).setLongitude(location.getLongitude()).setAddress(mapUtils.locationToAddress(currentLocation, requireActivity()));
                updateCurrentLocationMarker(currentLocationMarker, currentLocation);

                if (requireActivity() instanceof MapCallback) {
                    ((MapCallback) requireActivity()).onCurrentLocationChange(currentLocation);
                }

            }
        };
    }

    private void updateCurrentLocationMarker(Marker marker, CustomLocation location) {
        if (marker == null) {
            currentLocationMarker = setCurrentLocationMarker(location);
            if (mCircle != null) mCircle.setCenter(currentLocationMarker.getPosition());
            // Move the camera to the new location
            isFirstTime = false;
            mapUtils.moveCameraWitZoom(location.toLatLng());
        } else if (isFirstTime) {
            isFirstTime = false;
            mapUtils.updateMarkerLocation(currentLocationMarker, location, mCircle, true);
        } else mapUtils.updateMarkerLocation(currentLocationMarker, location, mCircle, false);
    }

    private Marker setCurrentLocationMarker(CustomLocation location) {
        return mapUtils.createMarker(location.toLatLng(), location.getName(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
    }

    @Override
    public Marker createMarker(LatLng latLng, String title, BitmapDescriptor icon) {
        return mapUtils.createMarker(latLng, title, icon);
    }

    @Override
    public void animateMarker(Marker marker, LatLng latLng) {
        mapUtils.animateMarker(marker, latLng);
    }

    @Override
    public void clearMap() {
        map.clear();
        mCircle = null;
    }

    @Override
    public void removeMarker(Marker marker) {
        if (marker == null) return;
        marker.remove();
    }

    @Override
    public void SearchLocation(SearchView searchView) {
        //the current location update are docent needed
//        stopLocationUpdates();
        CustomLocation locationFromSearch = new CustomLocation();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(requireActivity());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList == null || addressList.isEmpty()) {
                    Toast.makeText(requireContext(), "No results", Toast.LENGTH_LONG).show();
                    return false;
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                locationFromSearch.setLatitude(address.getLatitude()).setLongitude(address.getLongitude()).setAddress(address.getFeatureName());
                mapUtils.createMarker(latLng, address.getFeatureName(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                updateCurrentLocationMarker(currentLocationMarker, locationFromSearch);
//                currentLocation.setLatitude(latLng.latitude).setLongitude(latLng.longitude).setAddress(address.getAddressLine(0));
                SearchLocation.setLatitude(latLng.latitude).setLongitude(latLng.longitude).setAddress(address.getAddressLine(0));
//                if (requireActivity() instanceof MapCallback) {
//                    ((MapCallback) requireActivity()).onCurrentLocationChange(currentLocation);
//                }
                mapUtils.moveCameraWitZoom(latLng);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void setMarkerOnCurrentLocation() {
        currentLocationMarker = setCurrentLocationMarker(currentLocation);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapFragment != null) {
            mapFragment.onDestroy();
            mapFragment = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Start location updates
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates
        stopLocationUpdates();
    }
}
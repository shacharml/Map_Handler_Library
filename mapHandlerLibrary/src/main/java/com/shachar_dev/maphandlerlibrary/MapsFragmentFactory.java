package com.shachar_dev.maphandlerlibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class MapsFragmentFactory {


    public  MapHandler createMapFragment(Bundle savedInstanceState, FragmentManager fragmentManager, int containerId) {
        Fragment mapFragment;
        if (savedInstanceState == null) {
            mapFragment = new MapsFragment();
            fragmentManager.beginTransaction().replace(containerId, mapFragment, MapsFragment.class.getSimpleName()).commit();
        } else {
            mapFragment = fragmentManager.findFragmentByTag(MapsFragment.class.getSimpleName());
        }
        return (MapHandler) mapFragment;
    }
}

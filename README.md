# Map_Handler_Library [![](https://jitpack.io/v/shacharml/Map_Handler_Library.svg)](https://jitpack.io/#shacharml/Map_Handler_Library)

The MapHandler Library is a Java library for handling maps in Android applications.
It provides convenient methods for working with maps, such as creating markers, animating markers, showing circles on locations, capturing screen snapshots, and more.

# Features
   * Create markers on the map
   * Animate markers to a specific position
   * Show circles on current location or specified locations
   * Capture screen snapshots of the map
   * Clear the map
   * Set markers on the current location
   * Remove markers from the map
   * Search for a location using a search view

# Getting Started

## Installation

1) Add it in your root `build.gradle` at the end of repositories:

	    allprojects {
		repositories {
		  ...
		  maven { url 'https://jitpack.io' }
		}
	      }
      
------------------------------------------------------------------------------------------------------------

2) Add the following dependencies in you app level gradle file if not exists:

	    dependencies {
		  implementation 'com.github.shacharml:Map_Handler_Library:1.00.02'
		  implementation 'com.google.android.gms:play-services-maps:18.1.0'
		}

------------------------------------------------------------------------------------------------------------


## Usage

To use the `MapHandler` library in your Android application, follow these steps:

1) Add your API Key in the manifest:

        <application  .....  />
              <activity
                android:name=".MainActivity"
                android:exported="true">
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />
                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
            </activity>

            <meta-data
                android:name="com.google.android.geo.API_KEY"
                android:value="MAP_API_KEY" />
        </application>


2) Add a `frameView` to your `Activity` layout file:

        <FrameLayout
          android:id="@+id/main_fragment"
          android:layout_width="match_parent"
          android:layout_height="match_parent" />
        
        
3) In your activity, Add an Map Handler Attribute and implements  `MapUtils.OnMarkerClickListener, MapCallback` interface to receive map-related events :

        public class MainActivity extends AppCompatActivity implements MapCallback {
            // ...

            private MapHandler mapHandler;


            @Override
            public void onMapInitialized() {
                // Map initialization complete
            }

            @Override
            public void onCurrentLocationChange(CustomLocation currentLocation) {
                // Current location changed
            }

           @Override
            public void onMarkerClick(Marker marker) {
                //marker clicked
            }

            // ...
        }
      
     
 4) Initialize map handler in the `OnCreate` function:
          
           mapHandler = new MapsFragmentFactory().createMapFragment(savedInstanceState, getSupportFragmentManager(), R.id.main_fragment);


 5) Use the `MapHandler` methods to interact with the map:

      * Create a marker:
                
                  LatLng latLng = new LatLng(37.7749, -122.4194);
                  String title = "San Francisco";
                  BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                  Marker marker = mapHandler.createMarker(latLng, title, icon);

      * Animate a marker to a new position:
               
                  LatLng newPosition = new LatLng(37.3382, -121.8863);
                  mapHandler.animateMarker(marker, newPosition);  
          
      * Show a circle on the current location:
               
                  int radius = 100; // in meters
                  int strokeColor = Color.BLUE;
                  int fillColor = Color.TRANSPARENT;
                  int stroke = 2;
                  mapHandler.showCircleOnCurrentLocation(radius, strokeColor, fillColor, stroke);
      
      * Capture a screen snapshot of the map:
      
                  mapHandler.captureScreen(bitmap -> {
                        // Handle the captured bitmap
                    });

      * Clear the map:
                
                 mapHandler.clearMap();

      * Set a marker on the current location:
              
                mapHandler.setMarkerOnCurrentLocation();

      * Remove a marker from the map:
      
                 mapHandler.removeMarker(marker); 

      * Search for a location using a search view:
      
                  mapHandler.SearchLocation(searchView);

------------------------------------------------------------------------------------------------------------

## Example (Java)

	you can see the example in the priject that connect to this library.




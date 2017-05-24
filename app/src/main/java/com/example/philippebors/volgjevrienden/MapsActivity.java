package com.example.philippebors.volgjevrienden;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public static final String EXTRA_MESSAGE = "com.example.volgjevrienden.MESSAGE";
    private boolean followMe = false;



    /**
     * onCreate
     * -> Builds the map and adds the api. This method is called
     *    at the beginning of the app its lifecycle.
     *
     * @param savedInstanceState  - Contains the previous activity's saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * onResume
     * -> Called when the user returns to the activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    /**
     * onPause
     * -> The application can go on pause, eg. another app is opened or
     *    the user returns to his start menu.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }



    /**
     * onMapReady
     * -> Manipulates the map once available.
     *    This callback is triggered when the map is ready to be used.
     *    This is where we can add markers or lines, add listeners or move the camera. In this case,
     *    we just add a marker near Sydney, Australia.
     *    If Google Play services is not installed on the device, the user will be prompted to install
     *    it inside the SupportMapFragment. This method will only be triggered once the user has
     *    installed Google Play services and returned to the app.
     *
     *    @param googleMap  - Main class of the Google maps API and the entry point for all methods
     *                        related to the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* Here you can add some stuff for testing */
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); // not needed now
    }


    /**
     * onConnected
     * -> Once a connection has been made, this method is called to request the location of the user.
     *    If there exists no permission to use the phone's gps than it will be requested in a pop-up
     *    window. A location object is then updated to receive the latest location. This continues
     *    until a location has been found that is not null. It will then execute handleNewLocation
     *    to put a new marker at the new location.
     *
     * @param bundle  - Can hold all types of values to pass
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // change later
        mLocationRequest.setSmallestDisplacement(10);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
           LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    /**
     * handleNewLocation
     * -> Whenever a new location has been found,
     *    this method is called. It retrieves the
     *    user's current longi- and latitude and
     *    places a marker at that specific location.
     *
     * @param location  - The user's last known location
     */
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.clear(); /* This clears all markers, can be troublesome for multiple markers */
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    /**
     * onConnectionSuspended
     * -> Provides callbacks whenever connects or disconnects from
     *    the server.
     *
     * @param i  - The suspension cause
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }


    /**
     * onConnectionFailed
     * -> Tries to handle a failed connection. If unsuccessful, it shows
     *    the error message.
     *
     * @param connectionResult  - A result of a connection made (or not)
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                /* Start an Activity that tries to resolve the error */
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * onLocationChanged
     * -> Called whenever the user's location changes
     * @param location - The last known location
     */
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /**
     * sendMessage
     * -> Whenever the button is pressed, this method is called
     * @param view  - The view of the button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}

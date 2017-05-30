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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
    public static boolean loggedIn = false;

    /* Public values for the database */
    public static double myLastLongitude;
    public static double myLastLatitude;

    /* An ArrayList */
    ArrayList<String> students;

    /* JSON array */
    JSONArray result;

    private LatLngBounds.Builder builder;



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
        /* Laat de vriendenknop zien als je bent ingelogd */
        if (loggedIn) {
            View view = findViewById(R.id.button2);
            view.setVisibility(View.VISIBLE);
            View view2 = findViewById(R.id.button_menu3);
           // view2.setVisibility(View.GONE);
        }
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        updatePublicLatLong(currentLatitude, currentLongitude);
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.clear(); /* This clears all markers */
        mMap.addMarker(options);

        findData();


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /**
     * findData
     * -> Fills the array with the content of the database
     */
    private void findData() {
        students = new ArrayList<String>();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                            setFriendsOnMap(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        /* Creating a request queue */
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        /* Adding request to the queue */
        requestQueue.add(stringRequest);
    }


    /**
     * setFriendsOnMap
     * -> Gets the location of a person in the database and
     *    makes a new marker at that position on the map
     *
     * @param j -  The array in which the information is stored
     */
    private void setFriendsOnMap(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            double longitude = Double.parseDouble(getLongitude(i));
            double latitude = Double.parseDouble(getLatitude(i));
            String number = getNumber(i);
            LatLng latLng = new LatLng(latitude, longitude);

            /* Add a marker */
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(number);
            mMap.addMarker(options);
        }
    }


    /**
     * getStudents
     * -> Fills the array with the content of the database
     * @param j  -
     */
    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * getNumber
     * -> Returns the number of a person in the array at location postition
     * @param position  - The index of the array
     * @return  - The number of the person
     */
    public String getNumber(int position){
        String number="";
        try {
            JSONObject json = result.getJSONObject(position);
            number = json.getString(Config.TAG_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return number;
    }

    /* Same idea as getNumber */
    public String getLongitude(int position){
        String Longitude="";
        try {
            JSONObject json = result.getJSONObject(position);
            Longitude = json.getString(Config.TAG_LONGITUDE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Longitude;
    }

    /* Same idea as getNumber */
    public String getLatitude(int position){
        String latitude="";
        try {
            JSONObject json = result.getJSONObject(position);
            latitude = json.getString(Config.TAG_LATITUDE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latitude;
    }

    /**
     * updatePublicLatLong
     * -> Updates the public members with a new location so the database
     *    can use it.
     * @param currentLatitude  -  Last known latitude
     * @param currentLongitude -  Last known longitude
     */
    private void updatePublicLatLong(double currentLatitude, double currentLongitude) {
        myLastLatitude = currentLatitude;
        myLastLongitude = currentLongitude;
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
     * -> Tries to handle a failed connection. If unsuccessful, it logs
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


    /* All methods below this point are connected with buttons and will be called
       when the buttons are clicked */
    public void ToonAlleData(View view){
        Intent intent = new Intent(this, ToonAlleData.class);
        startActivity(intent);
    }

    public void writeDatabase(View view){
        Intent intent = new Intent(this, AccountRegistreren.class);
        startActivity(intent);
    }

    public void friendsButton(View view) {
        Intent intent = new Intent(this, ScrollingActivity.class);
        startActivity(intent);
    }

    public void ToonJeVrienden(View view) {
        Intent intent = new Intent(this, ToonJeVrienden.class);
        startActivity(intent);
    }

    public void Login(View view) {
        Intent intent = new Intent(this, AccountLogin.class);
        startActivity(intent);
    }
}

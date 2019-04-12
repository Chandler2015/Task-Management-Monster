package com.example.yanghanwen.taskmanagementmonster;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;


/*
 *
 *  * Copyright © 2018 CMPUT301W18T08, University of Alberta - All Rights Reserved.
 *  * You may use, distribute or modify this code under terms and conditions of Code of Student Behavior at
 *  *  University of Alberta.
 *  * You can find a copy of the license in this project, otherwise please contact at
 *  *   hyang4@ualberta.ca
 *
 *
 */


/**
 *  This activity is used to locate the current location of
 *  user, since the emulator is not able to track location itself, we have to manually
 *  assign a coordinate to it, then we have to retrieve the location that we have already
 *  assigned bu getting the last known location, passing the coordinate back to main
 *  activity.
 *
 *
 * layout: activity_location_service.xml
 *
 *  @author Hanwen Yang
 *  @version 1.0.0
 */
public class LocationServiceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    private FusedLocationProviderClient mFusedLocationClient;
    double latToSend, lngToSend = 0;
    private LocationManager locationManager;


    /**
     * Called when activity starts, we need obtain the current
     * location and initiate the Google map.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setMyLastKnownLocation();

    }


    /**
     * Mainly used to initiate the map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        intent = getIntent();

        double LatitudeGet = intent.getDoubleExtra("latitude", 0);
        double LongitudeGet = intent.getDoubleExtra("longitude", 0);
        String TitleToReceive = intent.getStringExtra("taskTitle");
        String StatusToReceive = intent.getStringExtra("status");

        mMap = googleMap;


        //TODO add tasks location here
        // Add a marker in current task location and move the camera
        LatLng TaskLocation = new LatLng(LatitudeGet, LongitudeGet);
        mMap.addMarker(new MarkerOptions().position(TaskLocation).title(TitleToReceive).snippet(StatusToReceive));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TaskLocation));



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        //https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
        //2018-03-30
        else {
            ActivityCompat.requestPermissions(LocationServiceActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }



    /**
     * Checking for location permission, if permitted by user,
     * go in to Google map, if permission is denied, close current
     * Google map.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(LocationServiceActivity.this, "Permission denied to access your location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    /**
     * This is used to get the current location which
     * have assigned before by user.
     */
    private void setMyLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            Log.d("TEST@KEVIN", "#####################");
                            if (location != null) {
                                // Logic to handle location object
                                Log.d("Latitude", Double.toString(location.getLatitude()));
                                Log.d("Lontitude", Double.toString(location.getLongitude()));
                                latToSend = location.getLatitude();
                                lngToSend = location.getLongitude();
                            }
                        }
                    });
        }
    }


    /**
     * Executed when back button is pressed, passing the
     * location information back to main activity for WOW factor
     * usage.
     */
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent();
        intent3.putExtra("latitudeSent", latToSend);
        intent3.putExtra("longitudeSent", lngToSend);
        setResult(Activity.RESULT_OK, intent3);
        super.onBackPressed();
    }
}


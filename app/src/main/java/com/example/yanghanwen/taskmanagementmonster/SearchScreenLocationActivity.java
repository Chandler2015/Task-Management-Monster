package com.example.yanghanwen.taskmanagementmonster;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
 * This activity mainly used for displaying tasks around
 * current location within 5 kilometers. We decided to drop a marker
 * to each location, and information(task name and status) is shown
 * when marker is clicked for a brief preview.
 *
 *
 * layout: activity_search_location.xml
 *
 * @author Hanwen Yang
 * @version 1.0.0
 */
public class SearchScreenLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> getCoor = new ArrayList<>();
    private ArrayList<String> taskname = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastKnownLocation;
    private ArrayList<String> distanceInMtrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("TEST@KEVIN", "#####################");
        setMyLastKnownLocation();
    }



    /**
     * Used to initiate the map, setting marker click event,
     * and checking for accessing location permission
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Intent intent = getIntent();

        getCoor = intent.getParcelableArrayListExtra("coordinates");
        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@", getCoor.toString());
        taskname = intent.getStringArrayListExtra("taskname");
        Log.d("$$$$$$$$$$$$$$$$$$$$$$$$$$$$", taskname.toString());
        status = intent.getStringArrayListExtra("status");


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            /**
             * Marker click event, show the task name
             * and status
             * @param marker
             * @return
             */
            @Override
            public boolean onMarkerClick(Marker marker) {
                 marker.showInfoWindow();

                 return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        //https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
        //2018-03-30
        else {
            ActivityCompat.requestPermissions(SearchScreenLocationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }



    /**
     * Setting up my current location, and add a blue dot
     * to current location for further reviewing
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    Toast.makeText(SearchScreenLocationActivity.this, "Permission denied to access your location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }



    /**
     * This is used to get the tasks with status "requested"
     * or "bidded" within 5km of my current location, if tasks is far away
     * from user location, it will still shown on the map for either "requested",
     * "bidded", "assigned" or "done" status
     */
    private void setMyLastKnownLocation(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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

                                //add a circle to show a range within 5km
                                mMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(),
                                        location.getLongitude())).radius(5000).strokeColor(Color.parseColor("#500084d3")).fillColor(Color.parseColor("#500084d3")));
                                mLastKnownLocation = location;

                                //compute the distance between current location and
                                //all tasks that have location information along with them
                                for(int j = 0; j < getCoor.size(); j++) {
                                    Location loc1 = new Location("");
                                    loc1.setLatitude(location.getLatitude());
                                    loc1.setLongitude(location.getLongitude());
                                    Location loc2 = new Location("");
                                    loc2.setLatitude(getCoor.get(j).latitude);
                                    loc2.setLongitude(getCoor.get(j).longitude);
                                    distanceInMtrs.add(Float.toString(loc1.distanceTo(loc2)));
                                }
                                Log.d("****^&%%%^%#$%#@#$#@$#@@@@@@@@@@@@@@@@@", distanceInMtrs.toString());

                                //if the task is within 5km range and either "requested" or "bidded"
                                //adding a marker to the location
                                for(int k = 0; k < distanceInMtrs.size(); k++) {
                                    if(Float.valueOf(distanceInMtrs.get(k)) <= 5000 && (status.get(k).equals("bidded") || status.get(k).equals("requested"))) {
                                        LatLng nearbyTasks = new LatLng(getCoor.get(k).latitude, getCoor.get(k).longitude);
                                        mMap.addMarker(new MarkerOptions().position(nearbyTasks).title(taskname.get(k)).snippet(status.get(k)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(nearbyTasks));


                                    } else if(Float.valueOf(distanceInMtrs.get(k)) > 5000) {
                                        //if task is away, adding a marker
                                        LatLng awayTasks = new LatLng(getCoor.get(k).latitude, getCoor.get(k).longitude);
                                        mMap.addMarker(new MarkerOptions().position(awayTasks).title(taskname.get(k)).snippet(status.get(k)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(awayTasks));
                                    }
                                }
                            }
                        }

                    });
            }
        }
    }

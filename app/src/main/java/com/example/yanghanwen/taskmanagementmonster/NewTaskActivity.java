package com.example.yanghanwen.taskmanagementmonster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;


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
 * Activity of creating new task, users are able to add a new task,
 * with specifying its name, description as well as its location if they
 * want(they can surely ignore that by not doing so).
 *
 * @version 1.0
 * @author Xiang Fan && Hanwen Yang && Tianyi Liang
 */

public class NewTaskActivity extends AppCompatActivity {

    public static NewTaskModel newTaskModel; // Model of this activity
    private EditText editTitle;         // EditText of new title
    private EditText editDescription;   // EditText of new description
    private Button createButton;        // create Task button
    private ImageButton setLocation;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback;
    public TextView CoorMsg;
    public TextView CityMsg;
    private Place mapPlace;
    private ConnectionCheck connectionCheck;


    /**
     * Firstly called when activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.newTask_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // create model for this activity
        newTaskModel = new NewTaskModel();

        editTitle = (EditText) findViewById(R.id.editTextNewTitle);
        editDescription = (EditText) findViewById(R.id.editTextNewDescription);

        createButton = (Button) findViewById(R.id.buttonNewCreate);
        Button imageButton = (Button) findViewById(R.id.buttonNewImage);



        // get value and create new task when button clicked
        createButton.setOnClickListener(new View.OnClickListener() {


            /**
             * create new task button action, system brings user to creating task page
             *, when device is offline, the system will stop uploading current task and automatically continue uploading
             * when device is back online. User can either choose to create a task with or without assigning
             * a location along with it.
             * @param view
             */
            @Override
            public void onClick(View view) {
                if(!(connectionCheck.isNetWorkAvailable(getApplicationContext()))){
                    for(int i = 0; i < 3; i++){
                        Toast.makeText(getApplicationContext(),"Oops,data will upload once connected.",
                                Toast.LENGTH_LONG).show();

                    }
                }
                if(mapPlace != null) {
                    String taskname = editTitle.getText().toString();
                    String description = editDescription.getText().toString();

                    LatLng coordinate = mapPlace.getLatLng();
                    newTaskModel.createNewTask(taskname, description, coordinate);
                    Intent intentService = new Intent(getApplicationContext(),TaskIntentService.class);
                    intentService.putExtra("mode","create");
                    startService(intentService);

                    finish();
                } else {
                    Toast.makeText(NewTaskActivity.this, "You did not specify any location on this", Toast.LENGTH_SHORT).show();
                    String taskname = editTitle.getText().toString();
                    String description = editDescription.getText().toString();

                    newTaskModel.createNewTask(taskname, description, null);

                    Intent intentService = new Intent(getApplicationContext(),TaskIntentService.class);
                    intentService.putExtra("mode","create");
                    startService(intentService);

                    finish();
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewTaskActivity.this,
                        ImageListActivity.class);

                intent.putExtra("mode", "new");

                startActivity(intent);
            }
        });


        setLocation = (ImageButton) findViewById(R.id.add_location);


//        https://developers.google.com/places/android-api/placepicker
//        2018/03/31
        setLocation.setOnClickListener(new View.OnClickListener() {

            /**
             * Implementing Google PlacePicker here for
             * user to select a location where the task at.
             * @param view
             */
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(NewTaskActivity.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if(!places.getStatus().isSuccess()) {
                    Log.d("test", "not getting place right");

                    //to prevent memory leak
                    places.release();
                    return;
                }
            }
        };

    }


    /**
     * Get the picked location coordinate here
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        CoorMsg = (TextView) findViewById(R.id.coordinateMessage);
        CityMsg = (TextView) findViewById(R.id.taskCity);

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                mapPlace = place;

                CoorMsg.setText("This task will be located at:" + "\n" + place.getLatLng().toString());
                CityMsg.setText("This task is at:" + "\n" + place.getAddress());

                String toastMsg = String.format("Place: %s", place.getLatLng());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Initializing menu
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.discard, menu);
        return true;
    }


    /**
     * Alertdialog to make sure if user want give up on this
     * edit
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.discard:

                AlertDialog.Builder dialog = new AlertDialog.Builder(NewTaskActivity.this);
                dialog.setTitle("Alert");
                dialog.setMessage("This is going to discard current task, this might be irretrievable");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    }
                });
                dialog.show();
                break;

            default:
                break;
        }
        return true;
    }


    /**
     * Checking if user want quit
     */
    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(NewTaskActivity.this);
        dialog.setTitle("Alert");
        dialog.setMessage("This is going to discard current task, this might be irretrievable");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                NewTaskActivity.super.onBackPressed();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });
        dialog.show();
    }

}

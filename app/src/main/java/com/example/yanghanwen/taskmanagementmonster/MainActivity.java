package com.example.yanghanwen.taskmanagementmonster;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashSet;


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
 * The activity control the main view of the app
 * , once user finish locating himself/herself, system
 * will give a recommended task list that within 2km of
 * distance.
 *
 *
 * layout: activity_main.xml
 *
 *
 * @author Xiang Fan && Hanwen Yang && Tianyi Liang
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_GET = 1;
    public static MainModel mainModel;  // model of this activity

    private FloatingActionButton newTaskButton;       // button to create new task
    private ImageButton searchButton;
    private Button providerButton;      // button to see my task as provider
    private Button requesterButton;     // button to see my task as requester
    private Button profileButton;       // button to see my profile
    private Button refreshButton;
    private DrawerLayout mdrawerlayout;
    private long firstPressed;
    private TextView usernameView, emailView;
    private ListView recommendedTasks;
    private ArrayList<Task> recommendedList = new ArrayList<>();
    private ArrayList<Task> TmpTasks = new ArrayList<>();
    private ArrayAdapter<Task> adapter;
    private double latitudeGet, longitudeGet;



    /**
     *  Firstly executed when activity starts, we define some
     *  click listeners here.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();

        String username = intent.getStringExtra("username");

        // create the model by given the username
        mainModel = new MainModel(username);

        if (mainModel.getUsername() == null) {

            finish();

            Toast.makeText(MainActivity.this,
                    "Login Fail",
                    Toast.LENGTH_SHORT).show();
        }

        newTaskButton = (FloatingActionButton) findViewById(R.id.newTaskButton);
        //searchButton = (ImageButton) findViewById(R.id.mainSearch);
        providerButton = (Button) findViewById(R.id.providerButton);
        requesterButton = (Button) findViewById(R.id.requesterButton);
        profileButton = (Button) findViewById(R.id.profileButton);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // change
        recommendedTasks = (ListView) findViewById(R.id.recommendedTasklist);
        // change

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.icon_list_white_24dp);
        }


        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        NewTaskActivity.class);

                startActivity(intent);
            }
        });


        // Drawerlayout select actions
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.profileButton:
                        Intent intent = new Intent(MainActivity.this,
                                EditprofileActivity.class);

                        startActivity(intent);
                        break;

                    case R.id.providerButton:
                        Intent intent0 = new Intent(MainActivity.this, MyTaskActivity.class);

                        // set the type as provider
                        intent0.putExtra("type", "pro");

                        startActivity(intent0);
                        break;

                    case R.id.requesterButton:
                        Intent intent2 = new Intent(MainActivity.this, MyTaskActivity.class);

                        // set the type as requester
                        intent2.putExtra("type", "req");

                        startActivity(intent2);
                        break;

                    case R.id.myLocationButton:

                        Intent intent1 = new Intent(MainActivity.this, LocationServiceActivity.class);

                        startActivityForResult(intent1, REQUEST_CODE_GET);
                        break;

                    case R.id.SearchButton:

                        Intent intent3 = new Intent(MainActivity.this, SearchActivity.class);

                        startActivity(intent3);
                        break;

                    default:
                }
                return true;
            }
        });

        View header = navView.getHeaderView(0);
        usernameView = (TextView) header.findViewById(R.id.username);
        emailView = (TextView) header.findViewById(R.id.mail);


        String DrawerUsername = MainActivity.mainModel.getUsername();
        String DrawerEmail = MainActivity.mainModel.getEmail();
        usernameView.setText(DrawerUsername);
        emailView.setText(DrawerEmail);

        //TODO WOW factor ------------------------------------------------------------------------------
        refreshButton = (Button) findViewById(R.id.refreshing);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
            }
        });

        recommendedTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Recommended list click action
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("test######", Integer.toString(i));

                Task task = recommendedList.get(i);
                String title = task.getTaskname();
                String requester = task.getUsername();

                Intent intent = new Intent(MainActivity.this,
                        DetailTaskActivity.class);

                intent.putExtra("mode", 1);
                intent.putExtra("title", title);
                intent.putExtra("requester", requester);

                startActivity(intent);
            }
        });


    }


    /**
     * Receiving the coordinate sending from LocationService activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET:
                if (resultCode == Activity.RESULT_OK) {
                    latitudeGet = data.getDoubleExtra("latitudeSent", 0);
                    longitudeGet = data.getDoubleExtra("longitudeSent", 0);
                    Log.d("^&^&^^&^&^^&^&^&^&^&^^&^^&^", Double.toString(latitudeGet) + " " +
                            Double.toString(longitudeGet));
                    break;
                }
            default:
        }

        //recommendedTasks = (ListView) findViewById(R.id.recommendedTasklist);
        adapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, recommendedList);
        recommendedTasks.setAdapter(adapter);

        ElasticSearch.GetTask getTask = new ElasticSearch.GetTask();
        ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
        ElasticSearch.IsExistTask isExistTask = new ElasticSearch.IsExistTask();


        //search all database
        String qTasks = "{\"query\" : {\"match_all\": {} }, \"from\":0, \"size\":1000 }";
        getTasks.execute(qTasks);

        try {
            TmpTasks = getTasks.get();
            Log.i("getting something new", TmpTasks.toString());
        } catch (Exception e) {
            Log.d("test!!!!!!!!!!", "something went wrong");
        }


        //if the task is within 2km and with status
        //either "requested" or "bidded" is shown on the main activity screen
        for (int i = 0; i < TmpTasks.size(); i++) {
            if (TmpTasks.get(i).getCoordinate() != null) {
                if (((Math.abs(Math.abs(TmpTasks.get(i).getCoordinate().latitude) - Math.abs(latitudeGet)) <= 0.018) &&
                        Math.abs((Math.abs(TmpTasks.get(i).getCoordinate().longitude)
                                - Math.abs(longitudeGet))) <= 0.018) &&
                        (!TmpTasks.get(i).getStatus().equals("assigned") && !TmpTasks.get(i).getStatus().equals("done"))) {
                    Log.d("getting a lot new", TmpTasks.get(i).toString());
                    Log.d("getting coordinate present", Double.toString(latitudeGet) + "\n" + Double.toString(longitudeGet));
                    recommendedList.add(TmpTasks.get(i));
                    Log.d("testtttttt", recommendedList.toString());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "We found you nearby tasks", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(recommendedList.isEmpty()) {
            Toast.makeText(MainActivity.this, "There is no nearby tasks around you within 2km", Toast.LENGTH_SHORT).show();
        }
        mdrawerlayout.closeDrawers();
    }

    //TODO end of WOW factor------------------------------------------------------------------------


    @Override
    protected void onStart() {
        super.onStart();
        Intent intentService = new Intent(getApplicationContext(),TaskIntentService.class);
        intentService.putExtra("mode","notify");
        startService(intentService);
    }

    /**
     * Double press to quit action
     */
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - firstPressed < 3000) {
            super.onBackPressed();//if do a double click within 3000 milliseconds, back to previous activity
        } else {
            Toast.makeText(MainActivity.this, "Press again to quit", Toast.LENGTH_SHORT).show();
            firstPressed = System.currentTimeMillis();
        }
    }


    /**
     * Initializing Toolbar action
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    /**
     * Toolbar item click action
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:
                mdrawerlayout.openDrawer(GravityCompat.START);
                break;

            case R.id.search_item:

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);

                startActivity(intent);
                break;

            default:
        }
        return true;
        }
    }


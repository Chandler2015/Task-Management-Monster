package com.example.yanghanwen.taskmanagementmonster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


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
 * MyTaskActivity is used for showing the ListView when user want to view his Tasks
 * the listview has to kinds:
 * for requester listview, there will be a filter to show bidding task or assigned task
 * if choose bidding task, list view shows status as "bidding",title,lowest bid
 * if choose assigned task,list view shows status as "assigned",its provider username,title,accepted bid
 *
 * for provider listview, there will be a filter to show bidding task or assigned task
 * if choose bidding task, list view shows status as "bidding",title, task requester username,lowest bid so far,my bid
 * if choose assigned task, list view shows status as "assigned",title,its task requester username,my accepted bid
 *
 * layout : activity_my_task.xml
 *
 * @author Tianyi Liang && Xixuan Song
 * @version 1.0
 */
public class MyTaskActivity extends AppCompatActivity {

    private int mode;
    // these ListView is named with "requester", But it used for requester & provider case
    private ListView requesterBiddingListView;
    private ListView requesterListView;
    private ListView requesterAssignedListView;

    private String OperationType;

    public static final int RETURN_MAIN = 20;

    // different adapter for adapter different kind of ListView
    private TwoGridsAdapter adapter;
    private ThreeGridsAdapter threeGridsAdapter;
    private FourGridsAdapter fourGridsAdapter;
    private FiveGridsAdapter fiveGridsAdapter;
    private FourGridsProviderAdapter fourGridsProviderAdapter;


    public static String currentUsername;


    //get whole task list from ES
    private ArrayList<Task>wholeTaskList = new ArrayList<>();

    //the arraylist for the task the currentUser has bidded
    private ArrayList<Task>providerTaskList = new ArrayList<>();

    // the arraylist for currentUser's requested task
    public ArrayList<Task>taskList = new ArrayList<>();

    // a temp arraylist to store data and adapt to ListView
    public ArrayList<Task>newTaskList = new ArrayList<>();

    // used for ElasticSearch
    private String userID;


    /**
     *
     * Executed when the activity starts
     * It creates three listview to store whole Task List, bidding task list, and assigned task list
     * It creates two button as filter
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);

        // initial all listview
        requesterBiddingListView = findViewById(R.id.requesterBiddingListView);
        requesterAssignedListView = findViewById(R.id.requesterAssignedListView);
        requesterListView = findViewById(R.id.RequesterTask);

        ActionBar bar = getSupportActionBar();

        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E47833")));

        // get incoming type (requester/provider)
        OperationType = getIntent().getStringExtra("type");
        if(OperationType.equals("req")){
            mode = 1;
        }else if (OperationType.equals("pro")){
            mode = 2;
        }



        // get current username
        currentUsername = MainActivity.mainModel.getUsername();


        if(OperationType.equals("req")){

            // Elastic Search for current user to get his posted tasksList
            userID = currentUsername;

            ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
            String query = "{\"query\" : {\"term\" : { \"username\" : \""+userID+"\" }}}";
            getTasks.execute(query);

            try{
                taskList = getTasks.get();


            }catch (Exception e){
                Log.i("Error", "Failed to get the tasks from the async object");
            }
            // adapter to adapt requester arrayList
            adapter = new TwoGridsAdapter(getApplicationContext(),taskList);
            requesterListView.setAdapter(adapter);

            // set counter to 0
            for(Task task : taskList){
                if(task.getCounter()==1){
                    task.setCounter(0);
                    ElasticSearch.AddTask addTask = new ElasticSearch.AddTask();
                    addTask.execute(task);
                }
            }

            /**
             * for clicking each items in listview
             */
            requesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Task task = taskList.get(i);
                    String title = task.getTaskname();
                    String requester = task.getUsername();

                    Intent intent = new Intent(MyTaskActivity.this,DetailTaskActivity.class);
                    intent.putExtra("mode",3);
                    intent.putExtra("title", title);
                    intent.putExtra("requester", requester);

                    startActivityForResult(intent, RETURN_MAIN);
                }
            });




        }


        else if (OperationType.equals("pro")){

            // ElasticSearch to get whole tasksList in db
            ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
            String query = "{\"query\" : {\"match\" : { \"bids.userName\" : {\"query\": \""+currentUsername+"\"}}}}";

            getTasks.execute(query);

            try{
                wholeTaskList = getTasks.get();
                Log.i("print wholeTaskList", wholeTaskList.toString());

            }catch (Exception e){
                Log.i("Error", "Failed to get the tasks from the async object");
            }


            // get the TaskList I bidded,i.e. my provider list
            for(Task task : wholeTaskList){

                providerTaskList.add(task);

            }
            Log.i("provider Task",providerTaskList.toString());

            adapter = new TwoGridsAdapter(getApplicationContext(),providerTaskList);
            //although it says it is requestorListView, but it actually is providedListView
            requesterListView.setAdapter(adapter);


            /**
             * for clicking each item in listview
             */
            requesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Task task = providerTaskList.get(i);
                    String title = task.getTaskname();
                    String requester = task.getUsername();

                    Intent intent = new Intent(MyTaskActivity.this,DetailTaskActivity.class);
                    intent.putExtra("mode",2);
                    intent.putExtra("title", title);
                    intent.putExtra("requester", requester);

                    startActivityForResult(intent, RETURN_MAIN);
                }
            });





        }
    }

    /**
     * Initialize filter menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mytask_menu,menu);
        return true;
    }

    /**
     * filter for bidded,assigned,all
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.assignedItem:
                Log.i("print","print something");
                if(OperationType.equals("req")){
                    Log.i("in swith case","assign");
                    requesterListView.setVisibility(View.GONE);
                    //requesterAssignedListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.GONE);
                    requesterAssignedListView.setVisibility(View.VISIBLE);


                    newTaskList.clear();
                    Log.i("print taskList",taskList.toString());

                    for(Task task : taskList){
                        if(task.getStatus().equals("assigned")){
                            newTaskList.add(task);

                        }
                    }
                    Log.i("print newTaskList", newTaskList.toString());

                    fourGridsAdapter = new FourGridsAdapter(getApplicationContext(),newTaskList);
                    requesterAssignedListView.setAdapter(fourGridsAdapter);


                    //for clicking each items in listview
                    requesterAssignedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task task = newTaskList.get(position);
                            String title = task.getTaskname();
                            String requster = task.getUsername();

                            Intent intent = new Intent(MyTaskActivity.this, DetailTaskActivity.class);
                            intent.putExtra("mode",3);
                            intent.putExtra("title",title);
                            intent.putExtra("requester",requster);

                            startActivityForResult(intent, RETURN_MAIN);
                        }
                    });
                }
                if(OperationType.equals("pro")){

                    requesterListView.setVisibility(View.GONE);
                    //requesterAssignedListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.GONE);
                    requesterAssignedListView.setVisibility(View.VISIBLE);

                    newTaskList.clear();
                    for(Task task : providerTaskList){
                        if(task.getStatus().equals("assigned")){
                            newTaskList.add(task);
                        }
                    }


                    fourGridsProviderAdapter = new FourGridsProviderAdapter(getApplicationContext(),newTaskList);
                    requesterAssignedListView.setAdapter(fourGridsProviderAdapter);

                    // for clicking each item in listview
                    requesterAssignedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Task task = newTaskList.get(i);
                            String title = task.getTaskname();
                            String requester = task.getUsername();

                            Intent intent = new Intent(MyTaskActivity.this,DetailTaskActivity.class);
                            intent.putExtra("mode",2);
                            intent.putExtra("title", title);
                            intent.putExtra("requester", requester);

                            startActivityForResult(intent, RETURN_MAIN);
                        }
                    });
                }
                break;

            case R.id.biddedItem:
                if(OperationType.equals("req")){

                    requesterListView.setVisibility(View.GONE);
                    requesterAssignedListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.VISIBLE);

                    newTaskList.clear();
                    for(Task task : taskList ){

                        if (task.getStatus().equals("bidded")){
                            Log.i("getStatus","got bidded");
                            newTaskList.add(task);

                        }
                    }



                    threeGridsAdapter = new ThreeGridsAdapter(getApplicationContext(),newTaskList);

                    requesterBiddingListView.setAdapter(threeGridsAdapter);


                    // for clicking each items in listview
                    requesterBiddingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Task task = newTaskList.get(i);
                            String title = task.getTaskname();
                            String requester = task.getUsername();

                            Intent intent = new Intent(MyTaskActivity.this, DetailTaskActivity.class);
                            intent.putExtra("mode",3);
                            intent.putExtra("title", title);
                            intent.putExtra("requester", requester);

                            startActivityForResult(intent, RETURN_MAIN);
                        }
                    });

                }

                if(OperationType.equals("pro")){
                    requesterListView.setVisibility(View.GONE);
                    requesterAssignedListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.GONE);
                    requesterBiddingListView.setVisibility(View.VISIBLE);

                    newTaskList.clear();
                    for(Task task:providerTaskList){
                        if(task.getStatus().equals("bidded")){
                            newTaskList.add(task);
                        }
                    }
                    fiveGridsAdapter = new FiveGridsAdapter(getApplicationContext(),newTaskList);
                    requesterBiddingListView.setAdapter(fiveGridsAdapter);

                    // for clicking each item in listview
                    requesterBiddingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Task task = newTaskList.get(i);
                            String title = task.getTaskname();
                            String requester = task.getUsername();

                            Intent intent = new Intent(MyTaskActivity.this,DetailTaskActivity.class);
                            intent.putExtra("mode",2);
                            intent.putExtra("title", title);
                            intent.putExtra("requester", requester);

                            startActivityForResult(intent, RETURN_MAIN);
                        }
                    });
                }
                break;

            case R.id.allItem:
                requesterBiddingListView.setVisibility(View.GONE);
                requesterAssignedListView.setVisibility(View.GONE);
                requesterListView.setVisibility(View.VISIBLE);

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requesCode, int resultCode, Intent data) {

        super.onActivityResult(requesCode, resultCode, data);

        // if there is a result from DetailBidActivity
        if (requesCode == RETURN_MAIN) {

            if (resultCode == RESULT_OK) {

                finish();
            }
        }

    }
}

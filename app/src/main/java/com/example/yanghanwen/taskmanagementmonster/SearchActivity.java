package com.example.yanghanwen.taskmanagementmonster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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
 * SearchActivity is used for searching a
 * task (or tasks) as a role of provider, it will provide
 * all the tasks and provider is able to either filter tasks to
 * whatever he want through four different status, and can also
 * search a task(s) by entering keywords of username.
 *
 * layout: activity_search.xml
 *
 * @author Wenhan Yang && Hanwen Yang
 * @version 1.0
 */
public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private FloatingActionButton discover;
    private ImageButton search_button;

    private long firstPressed;

    public ArrayList<Task> Tmp = new ArrayList<>();
    public ArrayList<Task> taskList = new ArrayList<>();
    public ArrayList<Task> allTaskList = new ArrayList<>();
    public ArrayAdapter<Task> adapter;
    ArrayList<Task> tasks = new ArrayList<>();

    public static final int RETURN_MAIN = 20;


    /**
     * Firstly executed when code starts going
     * mainly used for getting all tasks that are
     * available in elasticsearch server
     *
     * @param savedInstanceState
     * @throws ArrayIndexOutOfBoundsException
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) throws ArrayIndexOutOfBoundsException {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar bar = getSupportActionBar();

        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00E47833")));


        listView = (ListView) findViewById(R.id.Search_result);
        editText = (EditText) findViewById(R.id.discover_search);
        String keyWord = editText.getText().toString();

        Task task_test = new Task();
        ElasticSearch.GetTask getTask = new ElasticSearch.GetTask();
        ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
        ElasticSearch.IsExistTask isExistTask = new ElasticSearch.IsExistTask();

        //getting all tasks from elasticsearch server
        String qUsername = "{\"query\" : {\"match_all\": {} }, \"from\":0, \"size\":1000 }";
        getTasks.execute(qUsername);


        try {
            Tmp = getTasks.get();
            Log.i("getting something new", tasks.toString());
        } catch(Exception e) {
            Log.d("test!!!!!!!!!!", "something went wrong");
        }


        search_button = (ImageButton) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskSearch();
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Task task = taskList.get(i);
                String title = task.getTaskname();
                String requester = task.getUsername();

                Intent intent = new Intent(SearchActivity.this,
                        DetailTaskActivity.class);

                intent.putExtra("mode", 1);
                intent.putExtra("title", title);
                intent.putExtra("requester", requester);

                startActivityForResult(intent, RETURN_MAIN);
            }
        });

        discover = (FloatingActionButton) findViewById(R.id.discoverButton);

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTaskCoordinates();
            }
        });
    }


    //TODO get information from database instead of tasklist
    /**
     * passing an arrayList of coordinates of existing tasks
     */
    public void getTaskCoordinates() {

        ArrayList<LatLng> coor = new ArrayList<>();
        ArrayList<String> taskName = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        Log.d(")))))))))))))))))))", Integer.toString(Tmp.size()));

        for (int i = 0; i < Tmp.size(); i++) {

            if (Tmp.get(i).getCoordinate() != null) {
                coor.add(Tmp.get(i).getCoordinate());
                taskName.add(Tmp.get(i).getTaskname());
                status.add(Tmp.get(i).getStatus());
            } else {
                continue;
            }
        }
        Intent intent = new Intent(SearchActivity.this, SearchScreenLocationActivity.class);
        intent.putParcelableArrayListExtra("coordinates", coor);
        intent.putStringArrayListExtra("taskname", taskName);
        intent.putStringArrayListExtra("status", status);
        startActivityForResult(intent, RETURN_MAIN);
    }


    /**
     * setting backbutton action:
     * press once to recover all elements that had been removed(same functionality as recover button)
     * do a double press within 3000 milliseconds to return to the last activity
     */
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - firstPressed < 3000) {
            super.onBackPressed();
        } else {
            Toast.makeText(SearchActivity.this, "Press again to quit", Toast.LENGTH_SHORT).show();
            firstPressed = System.currentTimeMillis();
        }
    }


    public void TaskSearch() {

        String keyWord = editText.getText().toString();

        String searchQuery = "{\"query\" : {\"match\" : { \"taskname\" : {\"query\": \""+keyWord+"\", \"operator\": \""+ "and" +"\"}}}}";
        ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
        getTasks.execute(searchQuery);


        try {
            taskList = getTasks.get();
            Log.i("getting something new", taskList.toString());
        } catch(Exception e) {
            Log.d("test!!!!!!!!!!", "something wrong");
        }

        for(Task task: taskList) {
            if(!task.getStatus().equals("bidded") && !task.getStatus().equals("requested")) {
                taskList.remove(task);
            }
        }

        String searchQuery2 = "{\"query\" : {\"match\" : { \"description\" : {\"query\": \""+keyWord+"\", \"operator\": \""+ "and" +"\"}}}}";
        ElasticSearch.GetTasks getTasks2 = new ElasticSearch.GetTasks();
        getTasks2.execute(searchQuery2);

        try {
            tasks = getTasks2.get();
            Log.i("getting something new", tasks.toString());
        } catch(Exception e) {
            Log.d("test!!!!!!!!!!", "something wrong");
        }
        /**
         * for (Object x : two){
         if (!one.contains(x))
         one.add(x);
         }
         */

        for(Task task: tasks) {
            if(!task.getStatus().equals("bidded") && !task.getStatus().equals("requested")) {
                tasks.remove(task);
            }
        }

        for (Task task: tasks) {
            if (!taskList.toString().contains(task.toString())) {
                taskList.add(task);
            }
        }
        adapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1,taskList);
        listView.setAdapter(adapter);

    }


    /**
     * Initializing filter menu
     *
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    /**
     * Filter action, is able to filter in four different ways:
     * bidded, assigned, requested or done
     *
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()) { // filter by bidded task
                case R.id.item1:
                    int k = 0;
                    ArrayList<Task> tasks1 = new ArrayList<>();
                    while(k < taskList.size()) {
                        if(taskList.get(k).getStatus().equals("bidded")) {
                            tasks1.add(taskList.get(k));
                        }
                        ArrayAdapter<Task> adapter1 = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1,tasks1);
                        listView.setAdapter(adapter1);
                        k++;
                    }
                    break;

                case R.id.item2:
                    int a = 0;
                    ArrayList<Task> tasks2 = new ArrayList<>();
                    while(a < taskList.size()) { // filter by requested task
                        if(taskList.get(a).getStatus().equals("requested")) {
                            tasks2.add(taskList.get(a));
                        }
                        a++;
                    }
                    ArrayAdapter<Task> adapter2 = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1,tasks2);
                    listView.setAdapter(adapter2);
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



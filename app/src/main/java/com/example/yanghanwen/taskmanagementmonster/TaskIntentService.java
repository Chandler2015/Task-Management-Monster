package com.example.yanghanwen.taskmanagementmonster;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import android.os.Handler;
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
 * Created by Terrence on 03/04/2018.
 * This is the intent service for upload data or delete data from database
 * we use two singleton TaskList and DeleteTaskList as queue to store the task to be uploaded or deleted
 * then in this intent service, it runs in background and keep pushing data to database when connected.
 *
 * @author Tianyi Liang && Xixuan Song
 */

public class TaskIntentService extends IntentService{

    private static final String TAG = "Task intent service";

    private ConnectionCheck connectionCheck;

    private Handler iServiceHandler;

    private String username;
    private String description;
    private String title;
    private LatLng coordinate;
    private Task mTask;
    private String mode;

    private String userID;

    public ArrayList<Task>reqestorList = new ArrayList<>();



    public TaskIntentService(){
        super("TaskIntentService");
    }

    public void displayMessage(String message){

        final String msg = message;
        iServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<3;i++){

                    Toast.makeText(getApplicationContext(),msg,
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void displayNotification(String notify){
        final String notifyMsg = notify;
        iServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 6; i++){
                    Toast.makeText(getApplicationContext(),notifyMsg,Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        iServiceHandler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread thread = new Thread();
        mode = intent.getStringExtra("mode");
        Log.d("current mode",intent.getStringExtra("mode"));

        Log.i(TAG,"Service Started");
        if (connectionCheck.isNetWorkAvailable(getApplicationContext())){

            Log.i(TAG,"connected");
        }else{
            Log.i(TAG,"notConnected");


        }
        userID = MainActivity.mainModel.getUsername();
        while (true) {
            if (connectionCheck.isNetWorkAvailable(getApplicationContext())){
                /**if (TaskList.getInstance().getTasks().size() == 0) {
                 break;
                 }
                 Log.d("current task is what?","Value" + TaskList.getInstance().getTasks().get(0));
                 Task task = TaskList.getInstance().getTasks().get(0);
                 addTask.execute(task);
                 TaskList.getInstance().getTasks().remove(0);*/

                try {
                    ElasticSearch.DeleteTask deleteTask = new ElasticSearch.DeleteTask();
                    Task mTask = DeleteTaskList.getInstance().getTasks().get(0);
                    ElasticSearch.DeleteTask deleteTask1 = new ElasticSearch.DeleteTask();

                    Log.i("delet","1st");
                    deleteTask.execute(mTask);
                    Log.i("delete","2nd");
                    deleteTask1.execute(mTask);
                    Log.i("delete","2nd pass");

                    DeleteTaskList.getInstance().getTasks().clear();
                }catch (Exception e){
                    Log.i("delete for update","have nothing to delete");
                }

                if(mode.equals("notify")){
                    Log.i(TAG,"query for notify start");
                    ElasticSearch.GetTasks getTasks = new ElasticSearch.GetTasks();
                    String query = "{\"query\" : {\"term\" : { \"username\" : \""+userID+"\" }}}";
                    getTasks.execute(query);
                    try{
                        reqestorList = getTasks.get();
                    }catch (Exception e){
                        Log.i("Error", "Failed to get the tasks from the async object");
                    }
                    for(Task task : reqestorList){
                        if(task.getCounter()==1){
                            Log.i(TAG,"You have a task was bidded");
                            task.setCounter(0);
                            ElasticSearch.AddTask addTask = new ElasticSearch.AddTask();
                            addTask.execute(task);
                            displayNotification("You have a task was bidded");
                            break;
                        }
                    }
                    break;

                }

                if(mode.equals("create")){
                    Log.i("offline ","create mode");
                    for(Task task : TaskList.getInstance().getTasks()){
                        Log.d("current task",""+task);

                        if(taskExsit(task.getUsername(),task.getTaskname())){
                            Log.i("create a conflict task","conflick task!!!!!!!!");
                            Log.d("boolean",""+taskExsit(task.getUsername(),task.getTaskname()));
                            Log.d("current tilte","value"+task.getTaskname());
                            title = task.getTaskname();
                            Log.i("current task name","herrerere");
                            Log.d("current task name",""+title);

                            displayMessage("Oops," + title + "'s name is conflicted.Please try another one.");


                        }else{
                            if(taskExsit(task.getUsername(),task.getTaskname())){
                                Log.i("create a conflict task","conflick task");
                                Log.d("boolean",""+taskExsit(task.getUsername(),task.getTaskname()));

                            }else{
                                Log.i("not conflict","not conflict");
                                ElasticSearch.AddTask addTask = new ElasticSearch.AddTask();
                                addTask.execute(task);
                            }
                        }
                    }
                    TaskList.getInstance().getTasks().clear();
                    break;
                }else{
                    for(Task task : TaskList.getInstance().getTasks()){
                        Log.d("task in for loop","Value" + task);


                        ElasticSearch.AddTask addTask = new ElasticSearch.AddTask();
                        addTask.execute(task);

                        ElasticSearch.AddTask addTask1 = new ElasticSearch.AddTask();
                        addTask1.execute(task);
                    }
                    TaskList.getInstance().getTasks().clear();
                    break;
                }
            }
        }
    }

    public boolean taskExsit(String username, String taskname){
        ElasticSearch.IsExistTask isExistTask = new ElasticSearch.IsExistTask();
        isExistTask.execute(username + taskname);
        try{
            if(isExistTask.get()){
                return Boolean.TRUE;
            } else {return Boolean.FALSE;}
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }


}
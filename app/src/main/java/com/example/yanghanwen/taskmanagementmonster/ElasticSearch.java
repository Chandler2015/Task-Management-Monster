package com.example.yanghanwen.taskmanagementmonster;


import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;


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
 * Elastic Search
 *
 *
 * @author Wenhan Yang && Yuhang Xiong
 */
public class ElasticSearch {
    private static JestDroidClient client;

    public static class AddUser extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User...users) {
            verifySettings();

            for (User user:users){
                Index index = new Index.Builder(user).index("cmput301w18t08").type("user").id(user.getUserName()).build();
                Log.d("user name is", user.getUserName());
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("UserName", result.getId());
                    }
                    else {
                        Log.i("Error", "Elasticsearch was not able to add the user");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build");
                }

            }
            return null;
        }
    }

    public static class IsExist extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){
            verifySettings();

            User user = new User();

            Get get = new Get.Builder("cmput301w18t08", params[0]).type("user").build();
            Log.d("usertest", params[0]);

            try {
                JestResult result = client.execute(get);
                user = result.getSourceAsObject(User.class);
            } catch (Exception e) {
                Log.i("Error", e.toString());
                Log.i("fjaijfi","fjjfjfjfjjfjfffffff");
            }

            if (user == null) {
                return false;
            }

            return true;
        }
    }

    public static class GetUser extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            verifySettings();

            User user = new User();
            Get get = new Get.Builder("cmput301w18t08", params[0]).type("user").build();

            try{
                JestResult result = client.execute(get);
                user = result.getSourceAsObject(User.class);
            } catch (Exception e) {
                Log.i("Error", "Fail to connect to server");
            }
            return user;
        }

    }

    public static class UpdateUser extends  AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                Update update = new Update.Builder(user).index("cmput301w18t08").type("user").id(user.getUserName()).build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(update);
                    if (result.isSucceeded()) {
                        Log.d("User id : ", result.getId());
                    } else {
                        Log.i("Error", "Unable to update user");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the user");
                }
            }
            return null;
        }
    }

    public static class AddTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            verifySettings();

            for (Task task : tasks) {
                Index index = new Index.Builder(task).index("cmput301w18t08").type("task")
                        .id(task.getUsername() + task.getTaskname()).build();

                try {

                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Log.d("Success print something", result.getId());
                    } else {
                        Log.i("Error", "Fail to add task");
                    }
                } catch (Exception e) {

                    Log.i("Error", "Fail to build");
                }

            }
            return null;
        }
    }

    /**
     * How to test addTask
     * Task task = new Task(userName,title,description);
     if (IsExistTask(userName+Title)){
     Toast.makeText(this, "key conflict detected, unable to generate task", Toast.LENGTH_SHORT).show();
     }
     else {
     ElasticSearch.AddTask addTask
     = new ElasticSearch.AddTask();
     addTask.execute(task);
     */

    public static class IsExistTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){
            verifySettings();

            Task task = new Task();

            Get get = new Get.Builder("cmput301w18t08", params[0]).type("task").build();
            Log.d("tasktest", params[0]);

            try {
                JestResult result = client.execute(get);
                task = result.getSourceAsObject(Task.class);
            } catch (Exception e) {
                Log.i("Error", e.toString());
                Log.i("fjaijfi","fjjfjfjfjjfjfffffff");
            }

            try{
                if(task.getTaskname() == null){
                    return false;
                }
            }catch (Exception e){
                if(task == null){
                    return false;
                }
            }

            return true;
        }
    }

    public static class GetTask extends AsyncTask<String, Void, Task> {
        @Override
        protected Task doInBackground(String... params) {
            verifySettings();

            Task task = new Task();
            Get get = new Get.Builder("cmput301w18t08", params[0]).type("task").build();

            try{
                JestResult result = client.execute(get);
                task = result.getSourceAsObject(Task.class);
            } catch (Exception e) {
                Log.i("Error", "Fail to build");
            }
            return task;
        }

    }

    /**
     * How to test getTask(you should know username of requester and task title
     * For example(userName = "yxiong3", title = "1" get from editText
     * private String taskID
     * taskID = yxiong3 + title
     * isExistTask test if task exist
     * If yes
     * ElasticSearch.GetTask getTask = new ElasticSearch.GetTask();
     getTask.execute(taskID);
     try{
     task = getTask.get();
     Log.i("print something","should print information");
     }
     catch (Exception e) {
     Log.i("Error", "Fail to connect to server");
     }
     */

    public static class UpdateTask extends  AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            verifySettings();

            for (Task task : tasks) {
                Update update = new Update.Builder(task).index("cmput301w18t08").type("task").id(task.getUsername() + task.getTaskname()).build();


                try {

                    DocumentResult result = client.execute(update);
                    if (result.isSucceeded()) {
                        Log.d("Success print something", result.getId());
                    } else {
                        Log.i("Error", "Error!!!");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Failed to build");
                }
            }
            return null;
        }
    }

    public static class DeleteTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            verifySettings();

            for (Task task : tasks) {
                Delete delete = new Delete.Builder(task.getUsername() + task.getTaskname())
                        .index("cmput301w18t08").type("task").build();

                try {

                    DocumentResult result = client.execute(delete);
                    if (result.isSucceeded()) {
                        Log.d("Success print something", result.getId());
                    } else {
                        Log.i("Error", "Error!!!");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Failed to build");
                }
            }
            return null;
        }
    }

    public static class GetTasks extends AsyncTask<String, Void, ArrayList<Task>> {
        @Override
        protected ArrayList<Task> doInBackground(String... search_parameters) {
            verifySettings();
            ArrayList<Task> tasks = new ArrayList<Task>();

            // TODO Build the query

            String query = "";

            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("cmput301w18t08")
                    .addType("task")
                    .build();


            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<Task> foundtasks = result.getSourceAsObjectList(Task.class);
                    tasks.addAll(foundtasks);
                }
                else {
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return tasks;
        }
    }


    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}



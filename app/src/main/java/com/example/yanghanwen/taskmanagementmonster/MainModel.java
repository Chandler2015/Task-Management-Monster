package com.example.yanghanwen.taskmanagementmonster;

import android.util.Log;

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
 * MVC model for the main activity, as well as store the current user information
 *
 * @version 1.0
 */
public class MainModel {

    private User user;      // the user object contain information of user

    /**
     * Construct a model instance for the MainActivity by providing the current user's
     * username.
     *
     * @param username the current user's username
     */
    public MainModel(String username) {

        ElasticSearch.GetUser getUser = new ElasticSearch.GetUser();
        getUser.execute(username);

        try{
            user = getUser.get();
            Log.i("print something","should print information");
        }
        catch (Exception e) {
            Log.i("Error", "Fail to connect to server");
        }
    }

    /**
     * Return the current user's username
     *
     * @return
     */
    public String getUsername() {
            return user.getUserName();
    }

    /**
     * Return the current user's email
     *
     * @return
     */
    public String getEmail() {

        return user.getEmail();
    }

    /**
     * Return the current user's phone number
     *
     * @return
     */
    public String getPhoneNum() {

        return user.getPhoneNum();
    }

    /**
     * Change the current user's email and phone number to the new value, and update by elastic
     * search
     *
     * @param email the user's new email
     * @param phoneNum  the user's new phone number
     */
    public void updateUser(String email, String phoneNum) {

        user.setEmail(email);
        user.setPhoneNum(phoneNum);

        ElasticSearch.AddUser addUser = new ElasticSearch.AddUser();
        addUser.execute(user);
    }

}

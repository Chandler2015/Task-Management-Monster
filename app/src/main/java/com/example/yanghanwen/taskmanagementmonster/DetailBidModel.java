package com.example.yanghanwen.taskmanagementmonster;



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
 * Created by superfan1995 on 2018-03-13.
 */

import android.util.Log;

/**
 * MVC model for the Detail bid activity, compute and return required data.
 *
 * @author Xiang Fan
 *
 * @version 1.0
 */
public class DetailBidModel {

    private final int position;         // the position of the bid in the ArrayList bids of task
    private final String provider;      // the provider create the bid
    private String email;
    private String phone;
    private final String amount;        // the bid amount of bid

    /**
     * Construct a model instance for the DetailBidActivity
     *
     * Store the input position, provider, and amount for further operation.
     *
     * @param position position of bid
     * @param provider provider of bid
     * @param amount amount of bid
     */
    public DetailBidModel(int position, String provider, String amount) {

        this.position = position;
        this.provider = provider;
        this.amount = amount;

        this.email = "No Information";
        this.phone = "No Information";

        ElasticSearch.GetUser getUser = new ElasticSearch.GetUser();
        getUser.execute(provider);

        try {
            User user = getUser.get();

            this.email = user.getEmail();
            this.phone = user.getPhoneNum();
        }
        catch (Exception e) {
            Log.i("Error", "Fail to connect to server");
        }
    }

    /**
     * Return the position in the ArrayList bids of task
     *
     * @return position of bid
     */
    public int getPosition() {

        return position;
    }

    /**
     * Return the provider of the bid
     *
     * @return provider of bid
     */
    public String getProvider() {

        return provider;
    }

    /**
     * Return the amount of bid
     *
     * @return amount of bid
     */
    public String getAmount() {

        return amount;
    }

    /**
     * Get the user's email
     *
     * @return user's email
     */
    public String getEmail() {

        return email;
    }

    /**
     * Get the user's phone number
     *
     * @return user's phone number
     */
    public String getPhone() {

        return phone;
    }

}
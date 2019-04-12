package com.example.yanghanwen.taskmanagementmonster;

import android.view.View;

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
 * MVC model for the Detail task activity, compute and return required data. This is used in case
 * when the detail is view in the my task as provider
 *
 * @author Xiang Fan
 *
 * @version 1.0
 */
public class DetailTaskProviderModel extends DetailTaskModel {

    private String status;

    /**
     * Construct a model instance for the DetailTaskActivity that are used to view the detail
     * of task that user bid as provider.
     *
     * if the task's status is assigned, then this task is assigned to the user.
     *
     * @param title the title / taskname of task that need show detail information
     * @param requestor the username of user who is the requester of the task
     */
    public DetailTaskProviderModel (String title, String requestor) {

        super(title, requestor);

        status = super.task.getStatus();

        /*
        if (super.task.getStatus().equals("assigned")) {
            assigned = Boolean.TRUE;
        }
        else {
            assigned = Boolean.FALSE;
        }
        */

    }

    /**
     * Get the string showed on detailBidInformation TextView of activity_detail_task.
     *
     * If the task already assigned, then print accepted bid, else the user's lowest bid.
     *
     * @return String showed on detailBidInformation TextView
     */
    public String getBidInfo() {

        if (status.equals("assigned") || status.equals("done")) {

            return "Accepted Bid:";
        }

        else {

            return "Current Lowest Bid:";
        }
    }

    /**
     * Return task's lowest bid, if the task is assigned it will also the user's
     * accpeted bid
     *
     * @return String showed on detailBidLowest TextView
     */
    public String getBidLowest() {

        if (status.equals("assigned") || status.equals("done")) {

            Double myBid = super.task.getUserAmount(super.username);
            return "$ " + myBid.toString();
        }

        else {

            Double lowestBid = super.task.getLowestBid();
            return "$ " + lowestBid.toString();
        }

    }

    /**
     * Get the string showed on detailMyBid TextView of activity_detail_task, will only show
     * if the task is assigned
     *
     * @return String showed on detailMyBid TextView
     */
    public String getMyBidInfo() {

        if (status.equals("assigned")) {

            return "";
        }

        else{

            return "Current My Bid:";
        }
    }

    /**
     * Return the user's bid on this task, will only show when the task is assigned
     *
     * @return the user's bid in string
     */
    public String getMyBid() {

        if (status.equals("assigned")) {

            return "";
        }

        else {

            Double myBid = super.task.getUserAmount(super.username);
            return "$ " + myBid.toString();
        }
    }

    /**
     *  Return the text showed on the buttonDetail, will only show the option of modify
     *  the bid if task not yet assigned.
     *
     * @return modify the task string on button
     */
    public String getButtonText1() {

        if (status.equals("assigned")) {

            return "";
        }

        else {

            return "Modify My Bid";
        }
    }

    /**
     * Return the text showed on the buttonDetail2, will only show the option of decline
     * the bid if task not yet assigned.
     *
     * @return decline the task string on button
     */
    public String getButtonText2() {

        if (status.equals("assigned")) {

            return "";
        }

        else {

            return "Decline My Bid";
        }
    }

    /**
     * This function is not work for this model
     *
     * @return null
     */
    public ArrayList<Bid> getBidsList() {

        return null;
    }

    /**
     * Return the visibility of the detailBidInformation
     *
     * @return an int represent the visibility of the detailBidInformation
     */
    public int visibilityBidInfo() {

        return View.VISIBLE;
    }

    /**
     * Return the visibility of the detailBidLowest
     *
     * @return an int represent the visibility of the detailBidLowest
     */
    public int visibilityBidLowest() {

        return View.VISIBLE;
    }

    /**
     * Return the visibility of the detailMyBid
     *
     * @return an int represent the visibility of the detailMyBid
     */
    public int visibilityMyBidInfo() {

        if (status.equals("assigned")) {

            return View.GONE;
        }

        else if (status.equals("done")) {

            return View.GONE;
        }

        else {
            return View.VISIBLE;
        }
    }

    /**
     * Return the visibility of the viewDetailMyBid
     *
     * @return an int represent the visibility of the viewDetailMyBid
     */
    public int visibilityMyBid() {

        if (status.equals("assigned")) {

            return View.GONE;
        }

        else if (status.equals("done")) {

            return View.GONE;
        }

        else {
            return View.VISIBLE;
        }
    }

    /**
     * Return the visibility of the editTextDetail
     *
     * @return an int represent the visibility of the editTextDetail
     */
    public int visibilityEdit() {

        if (status.equals("assigned")) {

            return View.GONE;
        }

        else if (status.equals("done")) {

            return View.GONE;
        }

        else {
            return View.VISIBLE;
        }
    }

    /**
     * Return the visibility of the editTextDetailTitle
     *
     * @return an int represent the visibility of the editTextDetailTitle
     */
    public int visibilityEditTitle() {

        return View.GONE;
    }

    /**
     * Return the visibility of the editTextDetailDescription
     *
     * @return an int represent the visibility of the editTextDetailDescription
     */
    public int visibilityEditDescription() {

        return View.GONE;
    }

    /**
     * Return the visibility of the detailListView
     *
     * @return an int represent the visibility of the detailListView
     */
    public int visibilityListView() {

        return View.GONE;
    }

    /**
     * Return the visibility of the buttonDetail
     *
     * @return an int represent the visibility of the buttonDetail
     */
    public int visibilityChangeButton() {

        if (status.equals("assigned")) {

            return View.GONE;
        }

        else if (status.equals("done")) {

            return View.GONE;
        }

        else {

            return View.VISIBLE;
        }
    }

    /**
     * Return the visibility of the buttonDetail2
     *
     * @return an int represent the visibility of the buttonDetail2
     */
    public int visibilityDeclineButton() {

        if (status.equals("assigned")) {

            return View.GONE;
        }

        else if (status.equals("done")) {

            return View.GONE;
        }

        else {

            return View.VISIBLE;
        }
    }

    /**
     * Modify the user's bid on the task to the new value
     *
     * @param newValue The new value of the change
     */
    public void changeButtonAction (String newValue) {

        Double userbid = Double.parseDouble(newValue);

        super.task.modifyBid(super.username, userbid);

        // update task by elastic search
        super.taskUpdate();
    }

    /**
     * decline the user's bid on this task
     *
     * @param newValue the new value of the change
     */
    public void declineButtonAction (String newValue) {

        super.task.declineBid(super.username);

        // update task by elastic search
        super.taskUpdate();
    }

    /**
     * get the mode of viewing the image
     *
     * @return mode of image
     */
    public String getImageMode() {

        return "viewOnly";
    }

    /**
     * Set the visibility of image Button
     *
     * @return visibility of image Button
     */
    public int visibilityImageButton() {

        if (super.hasImages()) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * weather provider name is showed in the given place
     *
     * @return if provider name is showed
     */
    public Boolean showProvider() {

        return Boolean.FALSE;
    }

    /**
     * Get the visibility of delete button
     *
     * @return the visibility of delete button
     */
    public int visibilityDeleteButton() {

        return View.GONE;
    }



}

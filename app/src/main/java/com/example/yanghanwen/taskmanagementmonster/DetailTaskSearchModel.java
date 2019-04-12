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
 *  MVC model for the Detail task activity, compute and return required data. This is used in case
 *  when the detail is view in the search result
 *
 *  @author Xiang Fan
 *
 *
 * @version 1.0
 */
public class DetailTaskSearchModel extends DetailTaskModel {

    private Boolean hasBid;         // this task in bidded status
    private Boolean userBidded;     // weather user has bid this task
    private Double userBid;         // the user's bid amount

    /**
     * Construct a model instance for the DetailTaskActivity that are used to view the detail
     * of task that user bid as requester.
     *
     * All the necessary condition will be check for easier operation.
     *
     * @param title the title / taskname of task that need show detail information
     * @param requestor the username of user who is the requester of the task
     */
    public DetailTaskSearchModel (String title, String requestor) {

        super(title, requestor);

        hasBid = super.task.hasBid();

        userBid = super.task.getUserAmount(super.username);

        if (userBid == null) {

            userBidded = Boolean.FALSE;
        }

        else {

            userBidded = Boolean.TRUE;
        }
    }

    /**
     * Get the string showed on detailBidInformation TextView of activity_detail_task.
     *
     * @return String showed on detailBidInformation TextView
     */
    public String getBidInfo() {

        if ( hasBid ) {

            return "Current Lowest Bid:";
        }

        else {

            return "Task currently has no bid";
        }
    }

    /**
     * Return task's lowest bid
     *
     * @return String showed on detailBidLowest TextView
     */
    public String getBidLowest() {

        if ( hasBid ) {

            return "$ " + super.task.getLowestBid().toString();
        }

        else {

            return "";
        }
    }

    /**
     * Get the string showed on detailMyBid TextView of activity_detail_task
     *
     * @return String showed on detailMyBid TextView
     */
    public String getMyBidInfo() {

        if ( userBidded ) {

            return "Current My Bid:";
        }

        else {

            return "";
        }
    }

    /**
     * Return the current user's bid on this task, will only show when the task is bidded
     * by current user
     *
     * @return the current user's bid in string
     */
    public String getMyBid() {

        if ( userBidded ) {

            return "$ " + userBid.toString();
        }
        else {

            return "";
        }
    }

    /**
     *  Return the text showed on the buttonDetail
     *
     * @return modify the task string on button
     */
    public String getButtonText1() {

        if ( userBidded ) {

            return "Modify My Bid";
        }

        else {

            return "Bid This Task";
        }
    }

    /**
     * Return the text showed on the buttonDetail2
     *
     * @return decline the task string on button
     */
    public String getButtonText2() {

        if ( userBidded ) {

            return "Decline My Bid";
        }

        else {

            return "";
        }
    }

    /**
     * Return the list of all bids on this task
     *
     * @return the ArrayList of all bids on this task
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

        return  View.VISIBLE;
    }

    /**
     * Return the visibility of the detailBidLowest
     *
     * @return an int represent the visibility of the detailBidLowest
     */
    public int visibilityBidLowest() {

        if ( hasBid ) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the detailMyBid
     *
     * @return an int represent the visibility of the detailMyBid
     */
    public int visibilityMyBidInfo() {

        if ( userBidded )  {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the viewDetailMyBid
     *
     * @return an int represent the visibility of the viewDetailMyBid
     */
    public int visibilityMyBid() {

        if ( userBidded ) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the editTextDetail
     *
     * @return an int represent the visibility of the editTextDetail
     */
    public int visibilityEdit() {

        return View.VISIBLE;
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
     * Return the visibility of the buttonDetail
     *
     * @return an int represent the visibility of the buttonDetail
     */
    public int visibilityChangeButton() {

        return View.VISIBLE;
    }

    /**
     * Return the visibility of the buttonDetail2
     *
     * @return an int represent the visibility of the buttonDetail2
     */
    public int visibilityDeclineButton() {

        if ( userBidded ) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
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
     * modify the user's bid to the new value / create new bid on this task
     *
     * @param newValue The new value of bid
     */
    public void changeButtonAction (String newValue) {

        userBid = Double.parseDouble(newValue);

        if (userBidded) {

            super.task.modifyBid(super.username, userBid);
        }

        else {

            super.task.createNewBid(super.username, userBid);
            super.task.setCounter(1);
        }

        super.taskUpdate();

        hasBid = Boolean.TRUE;

        userBidded =Boolean.TRUE;
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

        hasBid = super.task.hasBid();

        userBid = null;

        userBidded = Boolean.FALSE;
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

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
public class DetailTaskRequestorModel extends DetailTaskModel {

    String status;

    /**
     * Construct a model instance for the DetailTaskActivity that are used to view the detail
     * of task that user bid as requester.
     *
     * @param title
     * @param requestor
     */
    public DetailTaskRequestorModel(String title, String requestor) {

        super(title, requestor);

        status = super.task.getStatus();
    }

    /**
     * Get the string showed on detailBidInformation TextView of activity_detail_task.
     *
     * @return String showed on detailBidInformation TextView
     */
    public String getBidInfo() {

        if (status.equals("requested")) {

            return "Task currently has no bid";
        }

        else if (status.equals("bidded")) {

            return "Current Lowest Bid:";
        }

        else {

            return "Task Provider:";
        }

    }

    /**
     * Return task's lowest bid
     *
     * @return String showed on detailBidLowest TextView
     */
    public String getBidLowest() {

        if (status.equals("requested")) {

            return "";
        }

        else if (status.equals("bidded")) {

            Double lowestBid = super.task.getLowestBid();
            return "$ " + lowestBid.toString();
        }

        else {

            Bid bid = super.task.getBid(0);
            return bid.getUserName();
        }

    }

    /**
     * Get the string showed on detailMyBid TextView of activity_detail_task, will only show
     * if the task is assigned
     *
     * @return String showed on detailMyBid TextView
     */
    public String getMyBidInfo() {

        if (status.equals("requested")) {

            return "";
        }

        else if (status.equals("bidded")) {

            return "";
        }

        else {

            return "Accepted Bid";
        }
    }

    /**
     * Return the assigned user's bid on this task, will only show when the task is assigned
     *
     * @return the assigned user's bid in string
     */
    public String getMyBid() {

        if (status.equals("requested")) {

            return "";
        }

        else if (status.equals("bidded")) {

            return "";
        }

        else {

            Bid bid = super.task.getBid(0);

            String assignUser = bid.getUserName();

            Double myBid = super.task.getUserAmount(assignUser);
            return "$ " + myBid.toString();
        }
    }

    /**
     *  Return the text showed on the buttonDetail
     *
     * @return modify the task string on button
     */
    public String getButtonText1() {

        if (status.equals("requested")) {

            return "Modify Title";
        }

        else if (status.equals("assigned")) {

            return "Task Complete";
        }

        else {

            return "";
        }
    }

    /**
     * Return the text showed on the buttonDetail2
     *
     * @return decline the task string on button
     */
    public String getButtonText2() {

        if (status.equals("requested")) {

            return "Modify Description";
        }

        else if (status.equals("assigned")) {

            return "Task Incomplete";
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

        return super.task.getBidList();
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

        if (status.equals("requested")) {

            return View.GONE;
        }

        else {

            return View.VISIBLE;
        }
    }

    /**
     * Return the visibility of the detailMyBid
     *
     * @return an int represent the visibility of the detailMyBid
     */
    public int visibilityMyBidInfo() {

        if (status.equals("requested")) {

            return View.GONE;
        }

        else if (status.equals("bidded")) {

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

        {

            if (status.equals("requested")) {

                return View.GONE;
            }

            else if (status.equals("bidded")) {

                return View.GONE;
            }

            else {

                return View.VISIBLE;
            }

        }
    }

    /**
     * Return the visibility of the editTextDetail
     *
     * @return an int represent the visibility of the editTextDetail
     */
    public int visibilityEdit() {

        return View.GONE;
    }

    /**
     * Return the visibility of the editTextDetailTitle
     *
     * @return an int represent the visibility of the editTextDetailTitle
     */
    public int visibilityEditTitle() {

        if (status.equals("requested")) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the editTextDetailDescription
     *
     * @return an int represent the visibility of the editTextDetailDescription
     */
    public int visibilityEditDescription() {

        if (status.equals("requested")) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the buttonDetail
     *
     * @return an int represent the visibility of the buttonDetail
     */
    public int visibilityChangeButton() {

        if (status.equals("requested")) {

            return View.VISIBLE;
        }

        else if (status.equals("assigned")) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * Return the visibility of the buttonDetail2
     *
     * @return an int represent the visibility of the buttonDetail2
     */
    public int visibilityDeclineButton()  {

        if (status.equals("requested")) {

            return View.VISIBLE;
        }

        else if (status.equals("assigned")) {

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

        if (status.equals("bidded")) {

            return View.VISIBLE;
        }

        else {

            return View.GONE;
        }
    }

    /**
     * modify the task's title to the new value / set the task to the done status
     *
     * @param newValue The new value of the change
     */
    public void changeButtonAction(String newValue) {

        if (status.equals("requested")) {

            //ElasticSearch.DeleteTask deleteTask = new ElasticSearch.DeleteTask();
            //deleteTask.execute(super.task);

            DeleteTaskList.getInstance().getTasks().add(super.task);

            Task newTask = new Task(super.username, newValue, super.task.getDescription(),
                    super.task.getCoordinate());

            ArrayList<String> imagesBase64 = super.task.getImagesBase64();
            newTask.setImagesBase64(imagesBase64);

            //ElasticSearch.AddTask addTask = new ElasticSearch.AddTask();
            //addTask.execute(newTask);
            TaskList.getInstance().getTasks().add(newTask);

            super.task = newTask;
        }

        else if (status.equals("assigned")) {

            super.task.setDone();

            status = "done";

            // update task by elastic search
            super.taskUpdate();
        }
    }

    /**
     * modify the task's title
     *
     * @param newValue the new value of the change
     */
    public void declineButtonAction(String newValue) {

        if (status.equals("requested")) {

            super.task.setDescription(newValue);

            super.queueUpdate();
        }

        else if (status.equals("assigned")) {

            super.task.emptyBids();

            status = "requested";

            super.taskUpdate();
        }

        // update task by elastic search
        //super.taskUpdate();
    }

    /**
     * Decline the bid at the certain position, and then update to the new status
     *
     * @param position the position of the bid in Arraylist bids
     */
    @Override
    public void declineBid(int position) {

        super.declineBid(position);

        status = super.getStatus();
    }

    /**
     * Assigned the bid at the certain position, then update to the new status
     *
     * @param position the position of the bid in Arraylist bids
     */
    @Override
    public void assignBid(int position) {

        super.assignBid(position);

        status = super.getStatus();
    }

    /**
     * get the mode of viewing the image
     *
     * @return mode of image
     */
    public String getImageMode() {

        if (status.equals("requested")) {

            return "myTask";
        }

        else {

            return "viewOnly";
        }
    }

    /**
     * Set the visibility of image Button
     *
     * @return visibility of image Button
     */
    public int visibilityImageButton() {

        if (status.equals("requested")) {

            return View.VISIBLE;
        }

        else if (super.hasImages()) {

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

        if (status.equals("requested")) {

            return Boolean.FALSE;
        }

        else if (status.equals("bidded")) {

            return Boolean.FALSE;
        }

        else {

            return Boolean.TRUE;
        }
    }

    /**
     * Get the visibility of delete button
     *
     * @return the visibility of delete button
     */
    public int visibilityDeleteButton() {

        return View.VISIBLE;
    }

}

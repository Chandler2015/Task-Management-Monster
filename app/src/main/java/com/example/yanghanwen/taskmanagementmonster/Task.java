package com.example.yanghanwen.taskmanagementmonster;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

 /** Object class of task
  *
  * Contain the information and operation method of the current task.
  *
  * @version 1.0
  */

public class Task {

    private String username;        // the username of task creator
    private String taskname;        // the name of the task
    private String status;          // current status of the task
    private String description;     // description of the task
    private ArrayList<Bid> bids;    // list of all bids on this task
    private LatLng coordinate;
    private ArrayList<String> imagesBase64;

    private int counter;            // for check if status is changed

     /**
      * Create an empty task object.
      *
      * This constructor is used for elastic search.
      */
    public Task () {

    }

     /**
      * Create a task object by using the input
      *
      * @param username string name of the task creator
      * @param taskname string name of the task
      * @param description string description of task
      */
    public Task (String username, String taskname , String description, LatLng coordinate) {

        this.username = username;
        this.taskname = taskname;
        this.description = description;
        // set the initial status as "requested"
        status = "requested";

        // initialize the list of bids object
        bids = new ArrayList<Bid>();

        this.coordinate = coordinate;

        imagesBase64 = new ArrayList<String>();

        counter = 0;
    }


    public int getCounter(){
        return this.counter;
    }

    public void setCounter(int adder){
        this.counter = adder;
    }

     /**
      * Return the username
      *
      * @return username of this task
      */
    public String getUsername () {

        return this.username;
    }

     /**
      * Return the taskname
      *
      * @return taskname of this task
      */
    public String getTaskname () {

        return taskname;
    }

     /**
      * Return the status
      *
      * @return current status of this task
      */
    public String getStatus () {

        return this.status;
    }

     /**
      * Return description
      *
      * @return description of this task
      */
    public String getDescription () {

        return this.description;
    }

     /**
      * Change current taskname to the new value
      *
      * @param taskname the new taskname of this task
      */
    public void setTaskname (String taskname) {

        this.taskname = taskname;
    }

     /**
      * Change current status to the new value
      *
      * @param status the new status of this task
      */
    public void setStatus (String status) {

        this.status = status;
    }

     /**
      * Change description of this task
      *
      * @param description the new description of this task
      */
    public void setDescription (String description) {

        this.description = description;
    }

     /**
      * Create a new bid object and add it into the bid list of this task
      *
      * If current status is requested, then change the status to bidded as there are a bid
      * on this task.
      *
      * @param bidder username of user that bid this task
      * @param amount amount of bid that user want to bid
      */
    public void createNewBid(String bidder, Double amount) {

        Bid bid = new Bid(bidder, amount);
        bids.add(bid);

        if (this.status.equals("requested")) {

            this.setStatus("bidded");
        }

        this.taskname = taskname;
    }

     /**
      * Judge this task has at least one bid, and return a boolean True is it has at least one
      * bid, else return false
      *
      * @return
      */
    public Boolean hasBid() {

        int size = bids.size();

        if (size == 0) {
            return Boolean.FALSE;
        }
        else {
            return Boolean.TRUE;
        }
    }

     /**
      * Return an ArrayList of bids for function that list all bids of this task in the listview.
      *
      * @return The bids list of this task
      */
    public ArrayList<Bid> getBidList() {

        return bids;
    }

     /**
      * By using a input username, get the user's bid amount on this task.
      *
      * If the user haven't bid on this task, then return null.
      *
      * @param bidder a user's username
      * @return the Double value of user's bid on this task
      */
    public Double getUserAmount(String bidder) {

        Double userAmount = null;

        if ( this.hasBid() ) {

            int maxSize = bids.size();

            for (int i = 0; i < maxSize; i = i + 1) {

                Bid bid = bids.get(i);

                if (bid.getUserName().equals(bidder) ) {

                    userAmount = bid.getAmount();
                    break;
                }
            }
        }

        return userAmount;
    }

     /**
      * Get the lowest bid amount in all bids of this task
      *
      * @return the lowest bid amount of this task
      */
    public Double getLowestBid() {

        Double result = null;

        if ( this.hasBid() ) {

            int maxSize = bids.size();

            for (int i = 0; i < maxSize; i = i + 1) {

                Bid bid = bids.get(i);

                if (result == null) {
                    result = bid.getAmount();
                }

                if (bid.getAmount() < result) {
                    result = bid.getAmount();
                }
            }

        }

        return result;

    }

     /**
      * Change the one of user's bid amount on this task
      *
      * @param bidder the username of modified bid's user
      * @param newAmount the new bid amount of the user's bid
      */
    public void modifyBid(String bidder, Double newAmount) {

        if ( this.hasBid() ) {

            int maxSize = bids.size();

            for (int i = 0; i < maxSize; i = i + 1) {

                Bid bid = bids.get(i);

                if (bid.getUserName().equals(bidder)) {

                    bid.setAmount(newAmount);
                    break;
                }
            }
        }
    }

     /**
      * Decline a user's bid on this task
      *
      * If after decline there are no bid on this task, reset the status to requested.
      *
      * @param bidder the username of the declined task's user
      */
    public void declineBid(String bidder) {

        if ( this.hasBid() ) {

            int maxSize = bids.size();

            for (int i = 0; i < maxSize; i = i + 1) {

                Bid bid = bids.get(i);


                if (bid.getUserName().equals(bidder)) {
                    bids.remove(bid);
                    break;
                }
            }
        }

        if (!this.hasBid()) {

            this.setStatus("requested");
        }
    }

     /**
      * Delete all bids on this task and reset the status to requested
      */
    public void emptyBids() {

        bids.clear();

        this.setStatus("requested");
    }

     /**
      * Return a bid in the specified position in the bids list
      *
      * @param position the position of the bid in the bids arrayList
      * @return The bid object at the specified position
      */
    public Bid getBid(int position) {

        Bid bid = null;

        if ( hasBid() ) {

            bid = bids.get(position);
        }

        return bid;

    }

     /**
      * Assigned the task to a user
      *
      * All the other bids on this task will be deleted, only the assigned user's bid
      * will remain in the bids list.
      *
      * @param username the username of assigned user
      */
    public void setAssigned (String username) {

        Double amount = getUserAmount(username);

        this.emptyBids();

        this.createNewBid(username, amount);

        this.setStatus("assigned");
    }

     /**
      * Set the task to done status.
      */
    public void setDone() {

        this.setStatus("done");
    }

     /**
      * return a string summarize the general information of the task
      *
      * @return an String used to describe the task in listView
      */
    public String toString() {
        return "Username: " + username + "\n" + "Title: " + taskname + "\n" + "Description: " + description + "\n" + "Status: " + status + "\n" + "Lowest bid: " + bids;
    }

     /**
      * get the coordinate
      *
      * @return LatLng coordinate
      */
     public LatLng getCoordinate() {

         return this.coordinate;
     }

     /**
      * store a Arraylist of Base64 String images
      *
      * @param imagesBase64 a ArrayList of Base64 image
      */
     public void setImagesBase64(ArrayList<String> imagesBase64) {

         this.imagesBase64 = new ArrayList<String>(imagesBase64);
     }

     /**
      * Get the Arraylist of string information of corresponding images
      *
      * @return Arrylist<String> contain the information of corresponding image
      */
     public ArrayList<String> getImageMessages () {

         int i = this.imagesBase64.size();
         int j = 0;

         ArrayList<String> messages = new ArrayList<String>();

         while (j < i) {

             messages.add("Images " + (j + 1));

             j = j + 1;

         }
         return messages;

     }

     /**
      * Clean all content in current image ArrayList
      */
     public void deleteAllImages() {

         this.imagesBase64 = new ArrayList<String>();
     }

     /**
      * Add a new image into image Arraylist after convert it to Base64
      *
      * @param imageMap The new added image in Bitmap form
      */
     public void addImage(Bitmap imageMap) {

         byte[] imageByteArray = BitmapToByteArray(imageMap);
         String imageBase64 = byteArrayToBase64(imageByteArray);

         this.imagesBase64.add(imageBase64);
     }

     /**
      * Given a position of image in the images, return the image in Bitmap form
      *
      * @param position the position of image in the images
      * @return the Bitmap form of the image
      */
     public Bitmap getImage(int position) {

         String imageBase64 = this.imagesBase64.get(position);

         byte[] imageByteArray = Base64ToByteArray(imageBase64);
         Bitmap imageMap = byteArrayToBitmap(imageByteArray);

         return imageMap;
     }

     /**
      * Given a position of image in the images, delete that image
      *
      * @param position the position of image in the images
      */
     public void deleteImage(int position) {

         this.imagesBase64.remove(position);
     }

     /**
      * Convert an bitmap image tp byte[]
      *
      * @param imageMap the image in bitmap
      * @return the same image in byte[]
      */
     private byte[] BitmapToByteArray(Bitmap imageMap) {

         // https://stackoverflow.com/questions/13758560/android-bitmap-to-byte-array-and-back-skimagedecoderfactory-returned-null
         // 2018-4-3
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         imageMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
         byte[] byteArray = stream.toByteArray();

         return byteArray;
     }

     /**
      * Convert a byte[] image to Base64 String
      *
      * @param imageByteArray byte[] image
      * @return Base64 String image
      */
     private String byteArrayToBase64(byte[] imageByteArray) {

         // https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
         // 2018-4-4
         String imageBase64 = Base64.encodeToString(imageByteArray, Base64.NO_WRAP);

         return imageBase64;
     }

     /**
      * onvert a byte[] image to Bitmap
      *
      * @param imageByteArray onvert a byte[] image to Bitmap
      * @return Bitmap image
      */
     private Bitmap byteArrayToBitmap(byte[] imageByteArray) {

         // https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
         // 2018-4-4
         Bitmap imagemap = BitmapFactory.decodeByteArray(imageByteArray, 0,
                 imageByteArray.length);

         return imagemap;
     }

     /**
      * Convert a Base64 String image to byte[]
      *
      * @param imageBase64 Base64 String image
      * @return byte[] image
      */
     private byte[] Base64ToByteArray(String imageBase64) {

         // https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
         // 2018-4-4
         byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);

         return imageByteArray;
     }

     /**
      * If is task has at least one image
      *
      * @return if the task has at least one image
      */
     public Boolean hasImages() {

         return !this.imagesBase64.isEmpty();
     }

     /**
      * return the ArrayList of all images
      *
      * @return ArrayList contain all images
      */
     public ArrayList<String> getImagesBase64() {

         return imagesBase64;
     }

     /**
      * if image has more than 10 picture, it has space to add more
      *
      * @return if more than 10 pictures
      */
     public Boolean imageSpace() {

         int base64Size = imagesBase64.size();

         if (base64Size < 10) {

             return Boolean.TRUE;
         }

         else {

             return Boolean.FALSE;
         }
     }

}


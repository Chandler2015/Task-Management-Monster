package com.example.yanghanwen.taskmanagementmonster;


import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import static org.junit.Assert.assertArrayEquals;



public class TaskTest extends ActivityInstrumentationTestCase2 {

    public TaskTest() {
        super(MainActivity.class); // this definitely not right, just for example
    }

    public void testGetUsername () {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        assertTrue(task.getUsername() == username);
    }

    public void testGetTaskname () {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        assertTrue(task.getTaskname() == taskname);
    }

    public void testGetStatus () {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        assertTrue(task.getStatus() == "requested");
    }

    public void testGetDescription () {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        assertTrue(task.getDescription() == description);
    }

    public void testSetTaskName(){

        int tid = 2;
        String username = "Tom";
        String username2 = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);
        task.setTaskname(username2);
        assertTrue(task.getTaskname() == username2);
    }

    public void testSetStatus(){

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        String status2 = "bidded";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);
        task.setStatus(status2);
        assertTrue(task.getStatus() == status2);
    }

    public void testSetDescription(){

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        String description2 = "this is a message 111";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);
        task.setDescription(description2);
        assertTrue(task.getDescription() == description2);
    }

    public void testHasBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bider1 = "B1";
        Double amount1 = 1.0;

        assertFalse(task.hasBid());

        task.createNewBid(bider1, amount1);

        assertTrue(task.hasBid());
    }

    public void testNewBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bidder1 = "B1";
        Double amount1 = 1.0;

        task.createNewBid(bidder1, amount1);

        assertTrue(task.hasBid());
        assertTrue(task.getUserAmount(bidder1).compareTo(amount1) == 0);
    }

    public void testGetUserBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);
        String bidder1 = "B1";
        Double amount1 = 1.0;

        String bidder2 = "B2";
        Double amount2 = 2.0;

        task.createNewBid(bidder1, amount1);

        assertTrue(task.getUserAmount(bidder1).compareTo(amount1) == 0);
    }

    public void testDeclineBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bider1 = "B1";
        Double amount1 = 1.0;

        task.createNewBid(bider1, amount1);
        task.declineBid(bider1);

        assertFalse(task.hasBid());
    }

    public void testModifyBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bider1 = "B1";
        Double amount1 = 1.0;
        Double amount2 = 2.0;

        task.createNewBid(bider1, amount1);
        task.modifyBid(bider1, amount2);

        assertTrue(task.getUserAmount(bider1).compareTo(amount2) == 0);
    }

    public void testGetLowestBid() {

        int tid1 = 2;
        int tid2 = 3;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bider1 = "B1";
        String bider2 = "B2";
        String bider3 = "B3";

        Double amount1 = 1.0;
        Double amount2 = 5.0;
        Double amount3 = 2.0;

        task.createNewBid(bider1, amount1);
        task.createNewBid(bider2, amount2);
        task.createNewBid(bider3, amount3);

        assertTrue(task.getLowestBid().compareTo(amount1) == 0);

        Task task2 = new Task(username, taskname, description, coordinate);

        task2.createNewBid(bider1, amount3);
        task2.createNewBid(bider2, amount2);
        task2.createNewBid(bider3, amount3);

        assertTrue(task2.getLowestBid().compareTo(amount3) == 0);

    }

    public void testGetBidList() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        ArrayList<Bid> bids = new ArrayList<>();
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        String bider1 = "B1";
        String bider2 = "B2";
        String bider3 = "B3";

        Bid bid1 = new Bid("B1", 1.0);
        Bid bid2 = new Bid("B2", 2.0);
        Bid bid3 = new Bid("B3", 3.0);

        bids.add(bid1);
        bids.add(bid2);
        bids.add(bid3);

        task.createNewBid("B1", 1.0);
        task.createNewBid("B2", 2.0);
        task.createNewBid("B3", 3.0);


        for(int i = 0; i < task.getBidList().size(); i++) {
            assertTrue(task.getBidList().get(i).toString().equals(bids.get(i).toString()));
        }
    }

    public void testEmptyBids() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        ArrayList<Bid> bids = new ArrayList<>();
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);


        task.createNewBid("B1", 1.0);
        task.createNewBid("B2", 2.0);
        task.createNewBid("B3", 3.0);

        task.emptyBids();

        assertTrue(bids.isEmpty());

    }

    public void testGetBid() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        double amount = 1.0;
        ArrayList<Bid> bids = new ArrayList<>();
        Bid bid = new Bid(username,amount);
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);


        task.createNewBid("B1", 1.0);
        task.createNewBid("B2", 2.0);
        task.createNewBid("B3", 3.0);

        Bid bid1 = new Bid("B1", 1.0);
        Bid bid2 = new Bid("B2", 2.0);
        Bid bid3 = new Bid("B3", 3.0);

        bids.add(bid1);
        bids.add(bid2);
        bids.add(bid3);

        bid = task.getBid(1);

        assertTrue(bid.toString().equals(bids.get(1).toString()));
    }

    public void testSetAssigned() {
        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        String status = "requested";
        ArrayList<Bid> bids = new ArrayList<>();
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);
        Task task1 = new Task(username, taskname, description, coordinate);

        task.createNewBid(username, 1.0);

        Bid bid1 = new Bid(username, 1.0);

        task.setAssigned(username);

        for(int j = 0; j < bids.size(); j++) {
           if(bids.get(j).getUserName().equals("Tom")) {
               assertTrue(task.getStatus().equals("assigned"));
           }
        }
    }

    public void testSetDone() {
        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        task.setDone();

        assertTrue(task.getStatus().equals("done"));
    }

    public void testGetCoordinate() {

        int tid = 2;
        String username = "Tom";
        String taskname = "task1";
        String description = "this is a message";
        LatLng coordinate = new LatLng(53.5444, -113.49);

        Task task = new Task(username, taskname, description, coordinate);

        assertTrue(task.getCoordinate() == coordinate);
    }
}

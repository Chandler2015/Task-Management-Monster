package com.example.yanghanwen.taskmanagementmonster;

/**
 * Created by songxixuan on 2018-02-26.
 */

import android.test.ActivityInstrumentationTestCase2;

public class BidTest extends ActivityInstrumentationTestCase2 {

    public BidTest() {
        super(Bid.class); // this definitely not right, just for example
    }

    public void testGetUsername() {

        String username = "Tom";
        double amount = 0;

        Bid bid = new Bid(username, amount);

        assertTrue(bid.getUserName() == username);
    }

    public void testGetAmount() {

        String username = "Tom";
        double amount1 = 15;
        double amount2 = 20.111;

        Bid bid1 = new Bid(username, amount1);
        Bid bid2 = new Bid(username, amount2);

        assertTrue(bid1.getAmount() == amount1);
        assertTrue(bid2.getAmount() == amount2);
    }

    public void testSetAmount() {

        String username = "Tom";
        double amount1 = 15.1;
        double amount2 = 16.1;

        Bid bid = new Bid(username, amount1);
        bid.setAmount(amount2);

        assertTrue(bid.getAmount() == amount2);
    }

}
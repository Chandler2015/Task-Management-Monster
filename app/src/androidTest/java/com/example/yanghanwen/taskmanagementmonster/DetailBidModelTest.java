package com.example.yanghanwen.taskmanagementmonster;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by yanghanwen on 2018-03-19.
 */

public class DetailBidModelTest extends ActivityInstrumentationTestCase2 {

    public DetailBidModelTest() {
        super(MainActivity.class);
    }

    public void testGetPosition() {

        int position = 1;
        String provider = "Nick";
        String amount = "3";

        DetailBidModel model = new DetailBidModel(position, provider, amount);

        assertTrue(model.getPosition() == position);
    }

    public void testGetProvider() {

        int position = 1;
        String provider = "Nick";
        String amount = "3";

        DetailBidModel model1 = new DetailBidModel(position, provider, amount);

        assertTrue(model1.getProvider().toString().equals(provider.toString()));
    }

    public void testGetAmount() {

        int position = 1;
        String provider = "Nick";
        String amount = "3";

        DetailBidModel model1 = new DetailBidModel(position, provider, amount);

        assertTrue(model1.getAmount().toString().equals(amount.toString()));
    }
}

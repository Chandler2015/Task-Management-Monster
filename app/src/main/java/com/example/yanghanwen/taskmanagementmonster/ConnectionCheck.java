package com.example.yanghanwen.taskmanagementmonster;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


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
 * Created by Terrence on 03/04/2018.
 * taken from https://stackoverflow.com/questions/30343011/how-to-check-if-an-android-device-is-online
 * 07/04/2018
 */

public class ConnectionCheck {
    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
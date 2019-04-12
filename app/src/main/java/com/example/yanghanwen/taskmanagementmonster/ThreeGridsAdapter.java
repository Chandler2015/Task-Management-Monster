package com.example.yanghanwen.taskmanagementmonster;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
 * This is a three grids adapter to adapt the arraylist into Listview
 * works for showing task title and task status and task lowest bid in ListView
 */
public class ThreeGridsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Task> mTaskList;


    public ThreeGridsAdapter(Context mContext,ArrayList<Task>mTaskList){
        this.mContext = mContext;
        this.mTaskList = mTaskList;
    }
    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTaskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.mytask_adpter_view_layout_3grids,null);

        TextView taskTitle3 = (TextView)v.findViewById(R.id.taskTitle3);
        TextView taskStatus3 = (TextView)v.findViewById(R.id.taskStatus3);
        TextView taskBid3 = (TextView)v.findViewById(R.id.taskBid3);

        taskTitle3.setText("Title: "+mTaskList.get(i).getTaskname());
        taskStatus3.setText("Status: "+mTaskList.get(i).getStatus());
        taskBid3.setText("Lowest Bid: "+Double.toString(mTaskList.get(i).getLowestBid()));

        return v;
    }
}
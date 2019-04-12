package com.example.yanghanwen.taskmanagementmonster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
 * Control the activity of listing all image of a task
 *
 * @author Xiang Fan
 *
 * @version 1.0
 */
public class ImageListActivity extends AppCompatActivity {

    String mode;

    public static final int UPDATE_DATA = 10;

    Button createButton;
    Button deleteAllButton;

    ListView imageListView;

    private ArrayList<String> messageList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {

            Toast.makeText(getApplicationContext(),
                    "Error: receive no input",
                    Toast.LENGTH_SHORT).show();

            finish();
        }

        mode = extras.getString("mode");

        createButton = (Button) findViewById(R.id.buttonNewImage);
        deleteAllButton = (Button) findViewById(R.id.buttonDeleteAllImage);

        imageListView = (ListView) findViewById(R.id.imageListView);

        initialize();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ImageListActivity.this,
                        DetailImageActivity.class);

                intent.putExtra("mode", mode);
                intent.putExtra("position", -1);


                startActivityForResult(intent, UPDATE_DATA);
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mode.equals("new")) {

                    NewTaskActivity.newTaskModel.deleteAllImages();
                }

                else if (mode.equals("myTask")) {

                    DetailTaskActivity.detailTaskModel.deleteAllImages();
                }

                finish();
            }
        });

        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ImageListActivity.this,
                        DetailImageActivity.class);

                if (mode.equals("new")) {

                    intent.putExtra("mode", "newView");
                }

                else if (mode.equals("myTask")) {

                    intent.putExtra("mode", "myTaskView");
                }

                else {

                    intent.putExtra("mode", mode);
                }

                intent.putExtra("position", i);

                startActivityForResult(intent, UPDATE_DATA);
            }
        });
    }

    /**
     * initialize the view according to the mode
     */
    private void initialize() {

        if (mode.equals("viewOnly")) {

            createButton.setVisibility(View.GONE);
            deleteAllButton.setVisibility(View.GONE);
        }

        updateImages();
    }

    /**
     * update the image ListView
     */
    private void updateImages() {

        if (mode.equals("new")) {

            messageList = NewTaskActivity.newTaskModel.getImageMessages();

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, messageList);

            imageListView.setAdapter(adapter);
        }

        else {

            messageList = DetailTaskActivity.detailTaskModel.getImageMessages();

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, messageList);

            imageListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requesCode, int resultCode, Intent data) {

        super.onActivityResult(requesCode, resultCode, data);

        // if there is a result from DetailBidActivity
        if (requesCode == UPDATE_DATA) {

            if (resultCode == RESULT_OK) {

                updateImages();
            }
        }

    }

}
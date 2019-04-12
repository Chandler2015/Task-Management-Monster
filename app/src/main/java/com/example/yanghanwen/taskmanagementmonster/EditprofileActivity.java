package com.example.yanghanwen.taskmanagementmonster;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


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
 * The activity control the view of detail information and operating on a user
 *
 * @author Xiang Fan
 */
public class EditprofileActivity extends AppCompatActivity {

    //init the parameters
    private TextView nameView;          // TextView of username

    private EditText emailEdit;         // EditView of email
    private EditText phoneNumEdit;      // EditView of phone num

    private ImageButton changeButton;        // the button to change information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        nameView = (TextView) findViewById(R.id.profileName);

        emailEdit = (EditText) findViewById(R.id.profileEmail);
        phoneNumEdit = (EditText) findViewById(R.id.profilePhoneNum);

        changeButton = (ImageButton) findViewById(R.id.profileEditButton);

        String username = MainActivity.mainModel.getUsername();
        String email = MainActivity.mainModel.getEmail();
        String phoneNum = MainActivity.mainModel.getPhoneNum();

        nameView.setText(username);
        emailEdit.setText(email);
        phoneNumEdit.setText(phoneNum);

        ActionBar bar = getSupportActionBar();

        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E47833")));

        // if change button clicked, update the user information
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newEmail = emailEdit.getText().toString();
                String newPhoneNum = phoneNumEdit.getText().toString();

                MainActivity.mainModel.updateUser(newEmail, newPhoneNum);

                finish();
            }
        });

    }

}

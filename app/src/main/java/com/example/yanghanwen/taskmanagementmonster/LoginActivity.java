package com.example.yanghanwen.taskmanagementmonster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
 * Activity for login, the user can use an exist username to enter the main activity
 * or can enter another activity to create a new username
 *
 * @author Wenhan Yang && Yuhang Xiong && Tianyi Liang
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editID;
    private Button loginButton;
    private Button registerButton;
    public ProgressDialog progressDialog;
    private ConnectionCheck connectionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editID = (EditText)findViewById(R.id.id);

        loginButton = (Button)findViewById(R.id.login);
        registerButton = (Button)findViewById(R.id.register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Logging you in...");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();


                String userId = editID.getText().toString();

                if(connectionCheck.isNetWorkAvailable(getApplicationContext())){

                    if (existedUser(userId)){

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Log.d("username", userId);
                        intent.putExtra("username",userId);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Online Login Success",
                                Toast.LENGTH_SHORT).show();
                    }

                    else {
                        progressDialog.dismiss();
                        progressDialog = null;
                        Toast.makeText(getApplicationContext(), "Username Not Exist",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    for(int i = 0; i < 6; i++){

                        Toast.makeText(getApplicationContext(),"Network is currently down.Please try later.",
                                Toast.LENGTH_LONG).show();

                    }

                }

            }

        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

//        https://stackoverflow.com/questions/8332127/displaying-a-progress-dialog-until-new-activity-loads
//        2018/03/31
        if(this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }


    // ? what is the purpose if this?
    private Boolean isUserIdLegal(String text){
        if (text.equals("")) {
            return Boolean.FALSE;
        }
        else{
            return Boolean.TRUE;
        }
    }

    /**
     * Check is the input username already exist
     *
     * @param name the username for check
     * @return weather the username exist
     */
    public boolean existedUser(String name) {
        ElasticSearch.IsExist isExist = new ElasticSearch.IsExist();
        isExist.execute(name);
        try {
            if (isExist.get()){
                Log.d("it exist!!!",name);
                return true;
            }
            else{
                Log.d("unidentified user",name);
                return false;
            }}
        catch (Exception e){
            return false;
        }

    }

}

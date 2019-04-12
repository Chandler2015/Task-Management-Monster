package com.example.yanghanwen.taskmanagementmonster;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by yanghanwen on 2018-03-19.
 */

public class MainModelTest extends ActivityInstrumentationTestCase2 {

    public MainModelTest() {
        super(MainActivity.class);
    }

    public void testGetUsername() {

        ElasticSearch.AddUser addUser = new ElasticSearch.AddUser();

        String Username = "kevin";
        String email = "1234@gmail.com";
        String phoneNum = "123456789";

        User user = new User(Username, email, phoneNum);
        MainModel model = new MainModel(Username);

        addUser.execute(user);

        assertTrue(model.getUsername().toString().equals(Username.toString()));
    }

    public void testGetEmail() {

        ElasticSearch.AddUser addUser = new ElasticSearch.AddUser();

        String Username = "kevin";
        String email = "1234@gmail.com";
        String phoneNum = "123456789";

        User user = new User(Username, email, phoneNum);
        MainModel model1 = new MainModel(Username);

        addUser.execute(user);

        assertTrue(model1.getEmail().toString().equals(email.toString()));
    }

    public void testGetPhoneNumber() {

        ElasticSearch.AddUser addUser = new ElasticSearch.AddUser();

        String Username = "kevin";
        String email = "1234@gmail.com";
        String phoneNum = "123456789";

        User user = new User(Username, email, phoneNum);
        MainModel model2 = new MainModel(Username);

        addUser.execute(user);

        assertTrue(model2.getPhoneNum().toString().equals(phoneNum.toString()));
    }

    public void testUpdateUser() {

        ElasticSearch.AddUser addUser = new ElasticSearch.AddUser();

        String Username = "kevin";
        String email = "1234@gmail.com";
        String phoneNum = "123456789";
        String updateEmail = "5678@gmail.com";
        String updatePhoneNum = "345646354";

        User user = new User(Username, email, phoneNum);
        MainModel model3 = new MainModel(Username);

        addUser.execute(user);

        model3.updateUser(updateEmail, updatePhoneNum);

        assertTrue(model3.getEmail().toString().equals(updateEmail.toString()) && model3.getPhoneNum().toString().equals(updatePhoneNum.toString()));
    }
}

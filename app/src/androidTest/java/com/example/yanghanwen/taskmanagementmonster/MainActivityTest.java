package com.example.yanghanwen.taskmanagementmonster;


import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;


public class MainActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private String testName;
    private String email;
    private String phoNum;

    public MainActivityTest(){super(LoginActivity.class);}

    public void setUp()throws Exception{
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testStart()throws Exception{
        AppCompatActivity activity = getActivity();
    }


    /**
     * test login
     * test add new task
     * test view my requesting task
     * test click list view item and see the detail
     */
    public void testBLogin(){
        LoginActivity activity = (LoginActivity)solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        testName = "xf444444";
        solo.enterText((EditText)solo.getView(R.id.id),testName);
        solo.clickOnButton("           Log in           ");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

        // keep going
        // Create new task
        solo.clickOnView(solo.getView(R.id.newTaskButton));
        solo.assertCurrentActivity("Wrong Activity",NewTaskActivity.class);

        solo.enterText((EditText)solo.getView(R.id.editTextNewTitle),"testTask");
        assertTrue(solo.waitForText("testTask"));
        solo.enterText((EditText)solo.getView(R.id.editTextNewDescription),"testTask");
        assertTrue(solo.waitForText("testTask"));

        solo.clickOnButton("Add Image");
        solo.assertCurrentActivity("Wrong Activity", ImageListActivity.class);
        solo.clickOnButton("New Image");
        solo.assertCurrentActivity("Wrong Activity", DetailImageActivity.class);
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity",NewTaskActivity.class);
        solo.clickOnButton("Create");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        
        solo.sleep(3000);
        // go to see my request task
        swipeToRight(); //https://github.com/CMPUT301F17T31/NoName/blob/master/NoName/app/src/androidTest/java/com/example/haotianzhu/noname/AddHabitTest.java
        solo.clickOnView(solo.getText("Requestor Tasks"));
        solo.assertCurrentActivity("Wrong Activity", MyTaskActivity.class);


        // click one item in list view, go to detail task activity.
        ListView mlist = (ListView)solo.getView(R.id.RequesterTask);
        View mView = mlist.getChildAt(0);
        solo.clickOnView(mView);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

        solo.clickOnView(solo.getText("My Location"));
        solo.assertCurrentActivity("Wrong Acitivity",LocationServiceActivity.class);
        solo.goBack();
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        swipeToRight();

        solo.clickOnView(solo.getText("Search a task"));
        solo.assertCurrentActivity("WrongActivity",SearchActivity.class);
        solo.enterText((EditText)solo.getView(R.id.discover_search),"testTask");
        assertTrue(solo.waitForText("testTask"));
        solo.clickOnView(solo.getView(R.id.search_button));//----------------------------
        ListView listView = (ListView)solo.getView(R.id.Search_result);
        Task task = (Task)listView.getItemAtPosition(0);
        String taskname = task.getTaskname();
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        assertTrue(solo.waitForText(taskname,1,3000));
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity",SearchActivity.class);
        solo.clickOnView(solo.getView(R.id.discoverButton));
        solo.assertCurrentActivity("Wrong Activity", SearchScreenLocationActivity.class);
        solo.goBack();
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

    }

    /**
     * This function is used to test registering as a new user, please make sure the username has
     * not been used by anyone else and username is no less than 8 chars
     */
    public void testARegister(){
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity",RegisterActivity.class);
        String username = "xf444444";
        solo.enterText((EditText)solo.getView(R.id.profileName),username);
        String email = "xf4@gmail.com";
        solo.enterText((EditText)solo.getView(R.id.profileEmail),email);
        String phone = "110 110 1110";
        solo.enterText((EditText)solo.getView(R.id.profilePhoneNum),phone);
        solo.clickOnView(solo.getView(R.id.save));
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);

        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity",RegisterActivity.class);
        String username1 = "xf555555";
        solo.enterText((EditText)solo.getView(R.id.profileName),username1);
        String email1 = "xf5@gmail.com";
        solo.enterText((EditText)solo.getView(R.id.profileEmail),email1);
        String phone1 = "110 110 1110";
        solo.enterText((EditText)solo.getView(R.id.profilePhoneNum),phone1);
        solo.clickOnView(solo.getView(R.id.save));
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
    }

    public void testCModifyProfile(){
        LoginActivity activity = (LoginActivity)solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        testName = "xf444444";
        solo.enterText((EditText)solo.getView(R.id.id),testName);
        solo.clickOnButton("           Log in           ");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.sleep(5000);
        swipeToRight();

        solo.clickOnView(solo.getText("Profile"));
        solo.assertCurrentActivity("WrongActivity",EditprofileActivity.class);

        email = "test@gmail.com";
        phoNum = "7801111111";

        solo.clearEditText((EditText)solo.getView(R.id.profileEmail));
        solo.clearEditText((EditText)solo.getView(R.id.profilePhoneNum));

        solo.enterText((EditText)solo.getView(R.id.profileEmail),email);
        solo.enterText((EditText)solo.getView(R.id.profilePhoneNum),phoNum);

        solo.clickOnView(solo.getView(R.id.profileEditButton));
        solo.assertCurrentActivity("WrongActivity",MainActivity.class);
    }

    public void testDBidAdd(){
        LoginActivity activity = (LoginActivity)solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        testName = "xf555555";
        solo.enterText((EditText)solo.getView(R.id.id),testName);
        solo.clickOnButton("           Log in           ");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        swipeToRight();

        solo.clickOnView(solo.getText("Search a task"));
        solo.assertCurrentActivity("WrongActivity",SearchActivity.class);
        solo.enterText((EditText)solo.getView(R.id.discover_search),"testTask");
        assertTrue(solo.waitForText("testTask"));
        solo.clickOnView(solo.getView(R.id.search_button));//----------------------------
        solo.sleep(1500);
        ListView listView = (ListView)solo.getView(R.id.Search_result);
        View mView = listView.getChildAt(0);

        solo.clickOnView(mView);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        solo.enterText((EditText)solo.getView(R.id.editTextDetail),"25");
        solo.clickOnButton("Bid This Task");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

        solo.clickOnView(solo.getText("Provider Tasks"));
        solo.assertCurrentActivity("Wrong Activity",MyTaskActivity.class);
        ListView listView2 = (ListView)solo.getView(R.id.RequesterTask);
        Task task = (Task)listView2.getItemAtPosition(0);
        String taskname = task.getTaskname();
        View mView2 = listView2.getChildAt(0);
        solo.clickOnView(mView2);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        assertTrue(solo.waitForText(taskname,1,3000));
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Acitivity",MainActivity.class);

    }

    public void testEBidModify(){
        LoginActivity activity = (LoginActivity)solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        testName = "xf555555";
        solo.enterText((EditText)solo.getView(R.id.id),testName);
        solo.clickOnButton("           Log in           ");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        swipeToRight();

        solo.clickOnView(solo.getText("Provider Tasks"));
        solo.assertCurrentActivity("Wrong Activity",MyTaskActivity.class);
        ListView listView2 = (ListView)solo.getView(R.id.RequesterTask);
        Task task = (Task)listView2.getItemAtPosition(0);
        String taskname = task.getTaskname();
        View mView2 = listView2.getChildAt(0);
        solo.clickOnView(mView2);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        assertTrue(solo.waitForText(taskname,1,3000));

        String new_amount = "15";
        solo.enterText((EditText)solo.getView(R.id.editTextDetail),new_amount);
        solo.clickOnButton("Modify My Bid");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
    }

    public void testFBidDelete(){
        LoginActivity activity = (LoginActivity)solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity",LoginActivity.class);
        testName = "xf555555";
        solo.enterText((EditText)solo.getView(R.id.id),testName);
        solo.clickOnButton("           Log in           ");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        swipeToRight();

        solo.clickOnView(solo.getText("Provider Tasks"));
        solo.assertCurrentActivity("Wrong Activity",MyTaskActivity.class);
        ListView listView2 = (ListView)solo.getView(R.id.RequesterTask);
        Task task = (Task)listView2.getItemAtPosition(0);
        String taskname = task.getTaskname();
        View mView2 = listView2.getChildAt(0);
        solo.clickOnView(mView2);
        solo.assertCurrentActivity("Wrong Activity",DetailTaskActivity.class);
        assertTrue(solo.waitForText(taskname,1,3000));

        solo.clickOnButton("Decline My Bid");
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
    }


//     @Override
//     public void tearDown()throws Exception{

//         solo.finishOpenedActivities();

//     }

    public void swipeToRight() {
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 0 ;
        float xEnd = width / 2;
        solo.drag(xStart, xEnd, height / 2, height / 2, 1);
    }


}

package com.example.yanghanwen.taskmanagementmonster;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by superfan1995 on 2018-03-06.
 */

public class NewTaskActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;

    public NewTaskActivityTest() {
        super(com.example.yanghanwen.taskmanagementmonster.NewTaskActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    public void testTask() {

        solo.assertCurrentActivity("Wrong Activity", NewTaskActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editTextNewTitle), "New Title!");

        solo.enterText((EditText) solo.getView(R.id.editTextNewDescription),
                "This is a description.");

    }

}

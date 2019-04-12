package com.example.yanghanwen.taskmanagementmonster;

/**
 * Created by superfan1995 on 2018-02-26.
 */

import android.test.ActivityInstrumentationTestCase2;
import java.util.Date;
/**
 * Created by Terrence on 2018/2/26.
 */

public class MessageTest extends ActivityInstrumentationTestCase2 {

    public MessageTest(){
        super(Message.class);
    }

    public void testGetMessage(){
        String username = "bob";
        Date date = new Date();
        String myMessage1 = "HI";
        String myMessage2 = "Bye";

        Message message1 = new Message(username,myMessage1,date);
        Message message2 = new Message(username,myMessage2,date);

        assertTrue(message1.getMessage() == myMessage1);
        assertTrue(message2.getMessage() == myMessage2);

    }

    public void testGetDate(){
        String username = "bob";
        String myMessage = "HI";
        Date myDate = new Date();

        Message message = new Message(username,myMessage,myDate);

        assertTrue(message.getDate() == myDate);

    }

    public void testSetMessage(){
        String username = "bob";
        String myMessage1 = "HI";
        String myMessage2 = "Bye";
        Date date = new Date();
        Message message = new Message(username,myMessage1,date);
        message.setMessage(myMessage2);

        assertTrue(message.getMessage() == myMessage2);
    }

    public void testSetDate(){
        String username = "bob";
        String myMessage = "HI";
        Date date1 = new Date();
        Date date2 = new Date();

        Message message = new Message(username,myMessage,date1);
        message.setDate(date2);
        assertTrue(message.getDate() == date2);


    }
}

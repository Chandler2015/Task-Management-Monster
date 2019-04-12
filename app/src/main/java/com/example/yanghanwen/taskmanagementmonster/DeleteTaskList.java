package com.example.yanghanwen.taskmanagementmonster;

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
 * Singleton used for store task
 * @author Tianyi Liang && Xixuan Song
 */
public class DeleteTaskList {
    private ArrayList<Task>tasks;
    public ArrayList<Task>getTasks(){return tasks;}
    private static final DeleteTaskList ourInstance = new DeleteTaskList();

    public static DeleteTaskList getInstance() {
        return ourInstance;
    }

    private DeleteTaskList() {tasks = new ArrayList<Task>();
    }
}
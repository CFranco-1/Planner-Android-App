package com.example.plannerapp;

import java.io.Serializable;

public class EventGetSet implements Serializable {
    private String task = null;
    private String description = null;
    private String date = null;
    private String tid = null;

    public EventGetSet(String task, String description, String date, String tid)
    {
        this.task = task;
        this.description = description;
        this.date = date;
        this.tid = tid;
    }

    public EventGetSet()
    {

    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTask(){
        return task;
    }
    public void setTask(String task) {
        this.task= task;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description= description;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date) {
        this.date= date;
    }
}

package com.example.yangwenhan.subbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yangwenhan on 2018/1/28.
 */

public class Subscription {
    private String name;
    private Float charge;
    private String comment;
    private String dateString;
    //  Generating arraylist to store objects.
    public static ArrayList<Subscription> Subscriptions = new ArrayList<Subscription>();

    public Subscription(String name, float charge, String dateString, String comment) {
        this.name = name;
        this.charge = charge;
        this.comment = comment;
        this.dateString = dateString;
    }

    @Override
//  organize data format of subscriptions that will be displayed on listview in MainActivity.
    public String toString() {

        return " " + getName()  + " "  + "[$" + getCharge() + "]" +" " + " "+ "modified on " + getDate();
    }

//  Common getters and setters for other class to have access to subscription info.
    public String getName(){
        return this.name;
    }

    public void setName(String name){this.name = this.name;}

    public String getDate(){

        return this.dateString;
    }

    public void setDate(String dateString){this.dateString = dateString;}

    public Float getCharge(){
        return this.charge;
    }

    public void setCharge(Float charge){this.charge = charge;}

    public String getComment(){
        return this.comment;
    }

    public void setComment(String comment){this.comment = comment;}

    }

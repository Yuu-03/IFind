package com.example.ifind;

import android.widget.TextView;

public class AppreciationItemHelperClass {

    String itemname;
    String date;
    String time;
    String imageURL;

    String key;


    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables
    public AppreciationItemHelperClass(String itemname, String date, String time, String imageURL) {
        this.itemname = itemname;
        this.date = date;
        this.time = time;
        this.imageURL = imageURL;

    }


    //getter and setters

    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public AppreciationItemHelperClass(){

    }

}

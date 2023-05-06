package com.example.ifind;

import android.widget.TextView;

public class ItemHelperClass {

    String itemname;
    String description;
    String location;
    String date;
    String time;
    String imageURL;

    String key;


    String item_name, item_desc, item_loc, item_date, item_time, imageUrl;


    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables
    public ItemHelperClass(String itemname, String description, String location, String date, String time, String imageURL) {
        this.itemname = itemname;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.imageURL = imageURL;

    }

    public ItemHelperClass(TextView item_name, TextView item_desc, TextView item_loc, TextView item_date, TextView item_time, String imageUrl) {
        this.item_name = itemname;
        this.item_desc = description;
        this.item_loc = location;
        this.item_date = date;
        this.item_time = time;
        this.imageUrl = imageURL;
    }

    //getter and setters

    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    public ItemHelperClass(){

    }

}

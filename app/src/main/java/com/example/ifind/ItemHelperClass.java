package com.example.ifind;

import android.widget.TextView;

import org.w3c.dom.Text;

public class ItemHelperClass {

    String itemname;
    String description;
    String location;
    String date;
    String time;
    String imageURL;

    String key, userID, name;


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

    public ItemHelperClass(String itemName, String itemDescription, String itemLocation, String dateFound, String timeFound, String imageURL, String userID) {
        this.itemname = itemName;
        this.description = itemDescription;
        this.location = itemLocation;
        this.date = dateFound;
        this.time = timeFound;
        this.imageURL = imageURL;
        this.userID = userID;}

    public ItemHelperClass(String name, String date, String time, String imageUrl, String userID_) {
        this.name = itemname;
        this.date = date;
        this.time = time;
        this.userID = userID_;

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
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID ;}

    public ItemHelperClass(){

    }

}

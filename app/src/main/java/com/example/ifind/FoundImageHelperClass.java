package com.example.ifind;

public class FoundImageHelperClass {

    String itemname, description, location, date, time, imageURL;


    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables
    public FoundImageHelperClass(String itemname, String description, String location, String date, String time, String imageURL) {

        this.itemname = itemname;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.imageURL = imageURL;

    }
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

    public FoundImageHelperClass(){

    }

}

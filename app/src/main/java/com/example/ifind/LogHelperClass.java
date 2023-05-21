package com.example.ifind;

public class LogHelperClass {
    String date, postType, time, userID, key;

    //empty constructor to avoid errors in the databas

    public LogHelperClass(String date, String postType, String time, String userID) {
        this.date = date;
        this.postType = postType;
        this.time = time;
        this.userID = userID;
    }


    //getter and setters


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LogHelperClass(){

    }

}

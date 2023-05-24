package com.example.ifind;

public class LogHelperClass {
    String postType, userID, key, timestamp;

    //empty constructor to avoid errors in the databas

    public LogHelperClass(String date, String postType, String time, String userID) {
        this.timestamp = timestamp;
        this.postType = postType;
        this.userID = userID;
    }


    //getter and setters


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
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

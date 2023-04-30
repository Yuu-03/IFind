package com.example.ifind;

public class FoundImageHelperClass {

    String imageURL, itemname;


    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables
    public FoundImageHelperClass(String imageURL) {

        this.imageURL = imageURL;
        this.itemname = itemname;

    }
    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }
    //getter and setters

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public FoundImageHelperClass(){

    }

}

package com.example.ifind;

public class LostImageHelperClass {

    String imageURL;
    String itemname;


    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables
    public LostImageHelperClass(String imageURL, String itemname) {

        this.imageURL = imageURL;
        this.itemname = itemname;
    }

    //getter and setters
    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LostImageHelperClass(){

    }

}

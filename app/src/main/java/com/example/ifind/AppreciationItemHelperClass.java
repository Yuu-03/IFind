package com.example.ifind;

public class AppreciationItemHelperClass {
    String itemname, description, department, datePosted, timePosted, personName, key, imageURL;
    public AppreciationItemHelperClass(String itemname, String description, String department, String datePosted, String timePosted, String personName,String imageURL) {
        this.itemname = itemname;
        this.description = description;
        this.department = department;
        this.datePosted = datePosted;
        this.timePosted = timePosted;
        this.personName = personName;
        this.imageURL = imageURL;
    }



    //empty constructor to avoid errors in the database

    //these are the constructors to cal for the variables


    //getter and setters


    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getPersonName() {
        return personName;
    }
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    public String getTimePosted() {
        return timePosted;
    }
    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public AppreciationItemHelperClass(){

    }

}

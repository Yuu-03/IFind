package com.example.ifind;

public class UserRecordHelperClass {
    //Here we will add the variables that we want to store in the database
    String fname, email, phone, key;

    //empty constructor to avoid errors in the database
    public UserRecordHelperClass() {

    }



    //these are the constructors to cal for the variables
    public UserRecordHelperClass(String fname, String email, String phone, String key) {
        this.fname = fname;
        this.email = email;
        this.phone = phone;
        this.key = key;

    }



    //getter and setters

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


package com.example.ifind;

public class ReadWriteUserDetails {
    public String fname, mname, lname, username, phone;

    //empty constructor to obtain the snapshot of data from the database
    public ReadWriteUserDetails(){};
    //constractor = initializes the variables of the class
    public ReadWriteUserDetails(String fname,String mname,String lname,String username,String phone) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.username = username;
        this.phone = phone;
    }
}



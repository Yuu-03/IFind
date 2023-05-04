package com.example.ifind;

public class ReadWriteUserDetails {
    public String fname, username,email, phone;

    //empty constructor to obtain the snapshot of data from the database
    public ReadWriteUserDetails(){};
    //constractor = initializes the variables of the class
    public ReadWriteUserDetails(String fname,String username,String email,String phone) {
        this.fname = fname;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}



package com.example.ifind;

public class UserAccHelperClass {
    //Here we will add the variables that we want to store in the database
    String fname, mname, lname, username, email, phoneNum,password;

    //empty constructor to avoid errors in the database
    public UserAccHelperClass() {

    }

    //these are the constructors to cal for the variables
    public UserAccHelperClass(String fname, String mname, String lname, String username, String email, String phoneNum, String password) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
    }



    //getter and setters

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

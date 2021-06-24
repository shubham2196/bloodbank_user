package com.example.bloodbank;

public class UserHelperClass {

    String username, emailId, mobileNo, password, address, pinCode,city, state, country,bloodGroup,gender,suffered,dob;

    public static String getCurrentUser_username() {
        return currentUser_username;
    }

    public static void setCurrentUser_username(String currentUser_username) {
        UserHelperClass.currentUser_username = currentUser_username;
    }

    static String currentUser_username;

    public  UserHelperClass(){}

    public UserHelperClass(String username, String emailId, String mobileNo, String password, String dob, String gender,String bloodGroup, String address ,String pinCode, String city, String state, String country, String suffered) {
        this.username = username;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.password = password;
        this.address = address;
        this.pinCode = pinCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.suffered = suffered;
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSuffered() {
        return suffered;
    }

    public void setSuffered(String suffered) {
        this.suffered = suffered;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

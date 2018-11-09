package com.xinde.storage.item;

public class CarrierInfo {
    private String userName;
    private String phoneNo;
    private String password;
    private String userID;
    private String callback;
    private String type = "mobile";

    public CarrierInfo(String userName, String phoneNo, String password, String userID, String callback) {
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.password = password;
        this.userID = userID;
        this.callback = callback;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CarrierInfo{" +
                "userName='" + userName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", password='" + password + '\'' +
                ", userID='" + userID + '\'' +
                ", callback='" + callback + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

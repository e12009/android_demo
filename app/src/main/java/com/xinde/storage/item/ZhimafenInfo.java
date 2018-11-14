package com.xinde.storage.item;

public class ZhimafenInfo {
    private String type = "zhimafenDirect";
    private String userName;
    private String phoneNo;
    private String userID;
    private String callback;


    public ZhimafenInfo(String userName, String phoneNo, String userID, String callback) {
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.userID = userID;
        this.callback = callback;
    }

    public String getType() {
        return type;
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

    @Override
    public String toString() {
        return "ZhimafenInfo{" +
                "type='" + type + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", userID='" + userID + '\'' +
                ", callback='" + callback + '\'' +
                '}';
    }
}

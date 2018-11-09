package com.xinde.resume;

public class UserNameAndIDItem {
    private String tid;
    private String userName;
    private String userID;

    public UserNameAndIDItem(String tid, String userName, String userID) {
        this.tid = tid;
        this.userName = userName;
        this.userID = userID;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserNameAndIDItem{" +
                "tid='" + tid + '\'' +
                ", userName='" + userName + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}

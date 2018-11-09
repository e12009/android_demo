package com.xinde.resume;

public class PasswordItem {
    private String tid;
    private String password;

    public PasswordItem(String tid, String password) {
        this.tid = tid;
        this.password = password;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordItem{" +
                "tid='" + tid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

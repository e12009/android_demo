package com.xinde.resume;

public class SmsCodeItem {
    private String tid;
    private String smsCode;

    public SmsCodeItem(String tid, String smsCode) {
        this.tid = tid;
        this.smsCode = smsCode;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "SmsCodeItem{" +
                "tid='" + tid + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}

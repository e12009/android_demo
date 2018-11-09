package com.xinde.reponse.taskresult;

import java.util.List;

public class CarrierResult {
    private UserInfo userinfo;
    private List<CallHistory> callHistory;
    private List<SmsHistory> smsHistory;
    private List<BillHistory> billHistory;
    private List<WebsiteHistory> websiteHistory;

    public CarrierResult() {
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<CallHistory> getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(List<CallHistory> callHistory) {
        this.callHistory = callHistory;
    }

    public List<SmsHistory> getSmsHistory() {
        return smsHistory;
    }

    public void setSmsHistory(List<SmsHistory> smsHistory) {
        this.smsHistory = smsHistory;
    }

    public List<BillHistory> getBillHistory() {
        return billHistory;
    }

    public void setBillHistory(List<BillHistory> billHistory) {
        this.billHistory = billHistory;
    }

    public List<WebsiteHistory> getWebsiteHistory() {
        return websiteHistory;
    }

    public void setWebsiteHistory(List<WebsiteHistory> websiteHistory) {
        this.websiteHistory = websiteHistory;
    }

    @Override
    public String toString() {
        return "CarrierResult{" +
                "userinfo=" + userinfo +
                ", callHistory=" + callHistory +
                ", smsHistory=" + smsHistory +
                ", billHistory=" + billHistory +
                ", websiteHistory=" + websiteHistory +
                '}';
    }
}

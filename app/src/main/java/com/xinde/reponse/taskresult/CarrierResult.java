package com.xinde.reponse.taskresult;

public class CarrierResult {
    private UserInfo userinfo;
    private CallHistory callHistory;
    private SmsHistory smsHistory;
    private BillHistory billHistory;
    private WebsiteHistory websiteHistory;

    public CarrierResult() {
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public CallHistory getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(CallHistory callHistory) {
        this.callHistory = callHistory;
    }

    public SmsHistory getSmsHistory() {
        return smsHistory;
    }

    public void setSmsHistory(SmsHistory smsHistory) {
        this.smsHistory = smsHistory;
    }

    public BillHistory getBillHistory() {
        return billHistory;
    }

    public void setBillHistory(BillHistory billHistory) {
        this.billHistory = billHistory;
    }

    public WebsiteHistory getWebsiteHistory() {
        return websiteHistory;
    }

    public void setWebsiteHistory(WebsiteHistory websiteHistory) {
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

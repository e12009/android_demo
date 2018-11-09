package com.xinde.storage.item;

public class AuthInfo {
    private String appSecret;
    private String appId;

    public AuthInfo(String appId, String appSecret) {
        this.appSecret = appSecret;
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "appSecret='" + appSecret + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}

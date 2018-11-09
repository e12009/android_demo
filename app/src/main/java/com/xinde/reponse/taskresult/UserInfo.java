package com.xinde.reponse.taskresult;

import java.util.Date;

public class UserInfo extends BasicType {
    private String phoneNo;
    private String name;
    private String identity;
    private String address;
    private Date openDate;
    private String carrier;
    private String inputIdentity;
    private String inputName;
    private String province;

    public UserInfo() {
    }

    @Override
    public int itemType() {
        return BasicType.TYPE_USERINFO;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getInputIdentity() {
        return inputIdentity;
    }

    public void setInputIdentity(String inputIdentity) {
        this.inputIdentity = inputIdentity;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "phoneNo='" + phoneNo + '\'' +
                ", name='" + name + '\'' +
                ", identity='" + identity + '\'' +
                ", address='" + address + '\'' +
                ", openDate=" + openDate +
                ", carrier='" + carrier + '\'' +
                ", inputIdentity='" + inputIdentity + '\'' +
                ", inputName='" + inputName + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}

package com.xinde.reponse.taskresult;

public abstract class BasicType {
    public static final int TYPE_USERINFO = 0x00;
    public static final int TYPE_CALL_DETAIL = 0x01;
    public static final int TYPE_SMS_DETAIL = 0x02;
    public static final int TYPE_BILL_DETAIL = 0x03;
    public static final int TYPE_NETFLOW_DETAIL = 0x04;
    public static final int TYPE_WEB_DETAIL = 0x05;

    public abstract int itemType();
}

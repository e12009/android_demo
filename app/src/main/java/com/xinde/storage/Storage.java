package com.xinde.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.xinde.storage.item.AuthInfo;
import com.xinde.storage.item.CarrierInfo;
import com.xinde.storage.item.ZhimafenInfo;

public class Storage {
    private static final String TAG = "Storage";

    // auth info
    private static final String AUHT_PREF = "AUTH_INFO";
    private static final String AUTH_ITEM_APPID = "appId";
    private static final String AUTH_ITEM_APPSECRET = "appSecret";

    // carrier entry name
    private static final String CARRIER_PREF = "CARRIER_AUTH_INFO";
    // zhimafen entry name
    private static final String ZHIMAFEN_PREF = "ZHIMAFEN_AUTH_INFO";

    // persisted data fields
    private static final String ITEM_USER_NAME = "userName";
    private static final String ITEM_PASSWORD = "password";
    private static final String ITEM_PHONENO = "phoneNo";
    private static final String ITEM_USERID = "userID";
    private static final String ITEM_CALLBACK = "callback";

    private static Storage ourInstance = new Storage();

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }

    public AuthInfo getAuthInfo(Context context) {
        if (null == context) {
            Log.e(TAG, "context is null");
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(AUHT_PREF, Context.MODE_PRIVATE);
        String appId = pref.getString(AUTH_ITEM_APPID, null);
        if (null == appId) {
            Log.e(TAG, "no auth info");
            return null;
        }

        String appSecret = pref.getString(AUTH_ITEM_APPSECRET, null);
        return new AuthInfo(appId, appSecret);

    }

    public void saveAuthInfo(Context context, AuthInfo authInfo) {
        if (null == context || null == authInfo) {
            Log.e(TAG, "context or authInfo is null");
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(AUHT_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(AUTH_ITEM_APPID, authInfo.getAppId().trim());
        editor.putString(AUTH_ITEM_APPSECRET, authInfo.getAppSecret().trim());
        editor.commit();
    }

    public CarrierInfo getCarrierInfo(Context context) {
        if (null == context) {
            Log.e(TAG, "context is null");
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(CARRIER_PREF, Context.MODE_PRIVATE);
        if (null == pref.getString(ITEM_USER_NAME, null)) {
            Log.e(TAG, "no carrier info");
            return null;
        }

        // parameter order is (String userName, String phoneNo, String password, String userID, String callback)
        return new CarrierInfo(
                pref.getString(ITEM_USER_NAME, null),
                pref.getString(ITEM_PHONENO, null),
                pref.getString(ITEM_PASSWORD, null),
                pref.getString(ITEM_USERID, null),
                pref.getString(ITEM_CALLBACK, null)
        );
    }

    public void saveCarrierInfo(Context context, CarrierInfo carrierInfo) {
        if (null == context || null == carrierInfo) {
            Log.e(TAG, "context or carrierInfo is null");
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(CARRIER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ITEM_USER_NAME, carrierInfo.getUserName().trim());
        editor.putString(ITEM_PHONENO, carrierInfo.getPhoneNo().trim());
        editor.putString(ITEM_PASSWORD, carrierInfo.getPassword().trim());
        editor.putString(ITEM_USERID, carrierInfo.getUserID().trim());
        editor.putString(ITEM_CALLBACK, carrierInfo.getCallback().trim());
        editor.commit();
    }

    public ZhimafenInfo getZhimafenInfo(Context context) {
        if (null == context) {
            Log.e(TAG, "context is null");
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(ZHIMAFEN_PREF, Context.MODE_PRIVATE);
        if (null == pref.getString(ITEM_USER_NAME, null)) {
            Log.e(TAG, "no zhimafen info");
            return null;
        }

        // parameter order is (String userName, String phoneNo, String userID, String callback)
        return new ZhimafenInfo(
                pref.getString(ITEM_USER_NAME, null),
                pref.getString(ITEM_PHONENO, null),
                pref.getString(ITEM_USERID, null),
                pref.getString(ITEM_CALLBACK, null)
        );
    }

    public void saveZhimafenInfo(Context context, ZhimafenInfo zhimafenInfo) {
        if (null == context || null == zhimafenInfo) {
            Log.e(TAG, "context or zhimfenInfo is null");
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(ZHIMAFEN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ITEM_USER_NAME, zhimafenInfo.getUserName().trim());
        editor.putString(ITEM_PHONENO, zhimafenInfo.getPhoneNo().trim());
        editor.putString(ITEM_USERID, zhimafenInfo.getUserID().trim());
        editor.putString(ITEM_CALLBACK, zhimafenInfo.getCallback().trim());
        editor.commit();
    }
}

package com.moontvdigital.app.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String PREFERENCE_KEY = "app_prefs";
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;
    private static PreferenceManager preferenceManager;

    private final String IP_ADDRESS = "IP_ADDRESS";
    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String MOBILE_NO = "MOBILE_NO";


    private PreferenceManager(Context mContext) {
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static PreferenceManager getInstance(Context mContext) {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager(mContext);
        }
        return preferenceManager;
    }

    public String getIpAddress() {
        return mSharedPreferences.getString(IP_ADDRESS, "");
    }

    public void setIpAddress(String ipAddress) {
        mEditor.putString(IP_ADDRESS, ipAddress).commit();
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, null);
    }

    public void setUserId(String userId) {
        mEditor.putString(USER_ID, userId).commit();
    }

    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        mEditor.putString(USER_NAME, userName).commit();
    }

    public String getMobileNo() {
        return mSharedPreferences.getString(MOBILE_NO, "");
    }

    public void setMobileNo(String mobileNo) {
        mEditor.putString(MOBILE_NO, mobileNo).commit();
    }

    public void clearUserPrefs() {
        mEditor.remove(USER_ID);
        mEditor.commit();
    }

    public void clearSharedPrefs() {
        mEditor.clear().commit();
    }
}

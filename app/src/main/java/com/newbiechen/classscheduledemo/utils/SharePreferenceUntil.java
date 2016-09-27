package com.newbiechen.classscheduledemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PC on 2016/9/26.
 */
public final class SharePreferenceUntil {
    
    private static final String FILE_LOGIN = "login";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_STATE = "state";
    
    private static SharePreferenceUntil sPreferenceUntil;
    private static SharedPreferences mPreference;
    
    private SharePreferenceUntil(Context context){
        //把文件的路径写死了- -。
        mPreference = context.getSharedPreferences(FILE_LOGIN,Context.MODE_PRIVATE);
    }

    public static void init (Context context){
        synchronized (SharePreferenceUntil.class){
            if (sPreferenceUntil == null){
                sPreferenceUntil = new SharePreferenceUntil(context);
            }
        }
    }

    public static String loadDataFromFile(String key){
       return mPreference.getString(key,"");
    }

    public static void saveData2File(String key,String value){
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key,value);
        editor.commit();
    }
    
}

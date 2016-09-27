package com.newbiechen.androidlib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by PC on 2016/9/9.
 * 简便的使用Toast
 * 方法：在继承Application类的类中初始化该Utils
 * 为什么不放在BaseActivity中，因为不光Activity需要用到该工具
 */
public final class ToastUtils {
    private static Context sContext;

    private ToastUtils(){
    }

    public static void init(Context context){
        sContext = context;
    }

    public static void makeText(String content,int time){
        Toast.makeText(sContext,content,time).show();
    }


}

package com.newbiechen.classscheduledemo;

import android.app.Application;
import android.widget.Toast;

import com.newbiechen.androidlib.utils.ToastUtils;
import com.newbiechen.classscheduledemo.net.CourseParse;
import com.newbiechen.classscheduledemo.utils.SharePreferenceUntil;

/**
 * Created by PC on 2016/9/26.
 */
public class AppService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferenceUntil.init(this);
    }
}

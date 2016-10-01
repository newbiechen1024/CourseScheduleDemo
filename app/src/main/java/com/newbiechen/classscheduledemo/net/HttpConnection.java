package com.newbiechen.classscheduledemo.net;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by PC on 2016/9/25.
 */
public class HttpConnection {
    private static final long TIME_OUT = 10000;

    private OkHttpClient mClient;
    private static HttpConnection sConnection;
    private final Map<String,List<Cookie>> mCookieMap = new HashMap<>();
    private HttpConnection(){
        mClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT,TimeUnit.MILLISECONDS)
                .cookieJar(new MyCookieJar())
                .build();
    }

    public static HttpConnection getInstance(){
        if (sConnection == null){
            sConnection = new HttpConnection();
        }
        return sConnection;
    }

    /*存储Cookie的类*/

    /**
     * 由于网站不能够持久化登陆，就直接将Cookie值放在HashMap中了
     */
    class MyCookieJar implements CookieJar{
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            mCookieMap.put(url.host(),cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookieList = mCookieMap.get(url.host());
            return cookieList == null ? new ArrayList<Cookie>() : cookieList;
        }
    }

    public void saveCookie(HttpUrl url,List<Cookie> cookies){
        //如果url相同则替换
        mClient.cookieJar().saveFromResponse(url,cookies);
    }

    public List<Cookie> getCookies(HttpUrl url){
        //未判断Http是否合法
        return mClient.cookieJar().loadForRequest(url);
    }

    /*默认使用异步加载*/
    public void connectUrl(Request request, Callback callback){
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    public interface HttpCallBack <T>{
        void callback(T data);
    }
}

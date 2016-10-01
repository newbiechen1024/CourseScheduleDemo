package com.newbiechen.classscheduledemo.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newbiechen.classscheduledemo.utils.URLManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PC on 2016/9/25.
 * 登陆到学校的教务网
 * 需要设置为单例模式
 */
public class LoginService {
    public static final String TAG = "LoginService";

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;
    //设置登陆的Post参数
    private String rudioButton = "学生";
    private String state = "dDwyODE2NTM0OTg7Oz7QBx05W486R++11e1KrLTLz5ET2Q==";
    private String button1 = "";
    private String language = "";
    private String hidPdrs = "";
    private String hidsc = "";

    private HttpConnection mConnection = HttpConnection.getInstance();

    public LoginService(Context context){
        mContext = context;
    }

    //获取验证码

    /**
     * 获取验证码的时候，会返回给客户端一个Cookie
     * 作用：获取验证码
     * @param httpCallBack
     */
    public <T extends Bitmap> void getCodesImg(final HttpConnection.HttpCallBack<T> httpCallBack) throws IOException{
        URL codeUrl = new URL(URLManager.URL_CODES);
        final Request request = new Request.Builder()
                .url(codeUrl)
                .build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"出错");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    //用流优化当收到的数据比较大
                    byte [] bytes = response.body().bytes();
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    final Bitmap bitmap =  BitmapFactory.decodeStream(bais);
                    //切换线程环境
                    changeEnvironment(httpCallBack,bitmap);
                    response.close();
                }
            }
        };
        mConnection.connectUrl(request,callback);
    }

    public <T> void login(final String username, String pwd, final String codes, final HttpConnection.HttpCallBack<T> httpCallBack) throws IOException{
        URL url = new URL(URLManager.URL_LOGIN);
        FormBody formBody = new FormBody.Builder()
                .add("__VIEWSTATE",state)
                .add("txtUserName",username)
                .add("TextBox2",pwd)
                .add("txtSecretCode",codes)
                .add("RadioButtonList1",rudioButton)
                .add("Button1",button1)
                .add("lbLanguage",language)
                .add("hidPdrs",hidPdrs)
                .add("hidsc",hidsc)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    boolean isLogin = false;
                    //判断是否登陆成功
                    if(CourseParse.
                            parseIsLoginSucceed(response.body().string())){
                        isLogin = true;
                    }
                    else {
                        //显示登陆失败。
                        isLogin = false;
                    }
                    changeEnvironment(httpCallBack,isLogin);
                    response.close();
                }
            }
        };
        mConnection.connectUrl(request,callback);
    }

    private <T> void changeEnvironment(final HttpConnection.HttpCallBack httpCallBack, final T data){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                httpCallBack.callback(data);
            }
        });
    }
}

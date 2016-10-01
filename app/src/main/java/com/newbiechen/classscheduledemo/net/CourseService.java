package com.newbiechen.classscheduledemo.net;

import android.os.Handler;
import android.util.Log;

import com.newbiechen.classscheduledemo.entity.Course;
import com.newbiechen.classscheduledemo.utils.URLManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PC on 2016/9/27.
 * 登陆成功后获取课程表
 */
public class CourseService {
    public static final String TAG = "CourseService";

    private final Handler mHandler = new Handler();
    private HttpConnection mConnection = HttpConnection.getInstance();

    public <T extends List<Course>> void getCourse(String username, final HttpConnection.HttpCallBack<T> callBack) throws IOException {
        //因为xh为学号，需要自己设置
        String urlCls = URLManager.URL_CLS.replace("XH",username);
        //重点：因为该网站实现的是动态跳转（有没有发现跳转到课表页面，但是网址没有变化）
        //必须添加Referer到Http头，Referer指的是当前页面的网址，否则会拒绝访问
        String referer = URLManager.URL_REFERER.replace("XH",username);
        Log.d(TAG,urlCls);
        URL url = new URL(urlCls);
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Referer",referer);
        builder.url(url);
        final Request request = builder.build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络较差环境下的问题
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //登陆到课表，并解析课程表的资源
                if (response.isSuccessful()){
                    //其实这里可以使用InputStream进行优化的。以后再说吧
                    final String clsDocument = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            T stuClassList = (T) CourseParse.parsePersonal(clsDocument);
                            callBack.callback(stuClassList);
                        }
                    });
                    response.close();
                }
            }
        };
        mConnection.connectUrl(request,callback);
    }
}

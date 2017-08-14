package com.study.lijia.gank.net;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lijia on 17-8-9.
 */

public class GankTask {

    private static GankTask mInstance;
    private GankService mService;

    public static GankTask getInstance() {
        if (mInstance == null) {
            mInstance = new GankTask();
        }
        return mInstance;
    }

    private GankTask() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //获得请求信息，此处如有需要可以添加headers信息
                Request request = chain.request();
                //打印请求信息
                Logger.e("url:" + request.url());

                //记录请求耗时
                long startNs = System.nanoTime();
                okhttp3.Response response;
                try {
                    //发送请求，获得相应，
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }
                long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
                //打印请求耗时
                Logger.e("耗时:" + tookMs + "ms");
                //使用response获得headers(),可以更新本地Cookie。
                Headers headers = response.headers();
                Logger.e(headers.toString());

                //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
                ResponseBody responseBody = response.body();

                //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                return response;
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient.build())
                .build();

        mService = retrofit.create(GankService.class);
    }

    public GankService getGankService() {
        return mService;
    }
}

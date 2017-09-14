package com.study.lijia.gank.net;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.study.lijia.gank.data.GankCategoryResult;
import com.study.lijia.gank.data.GankDailyResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Gank请求管理类
 * Created by lijia on 17-8-9.
 */

public class GankManager {

    private static final String BASE_GANK_URL = "http://gank.io/api/";

    private static GankManager mInstance;
    private GankService mService;

    public static GankManager getInstance() {
        if (mInstance == null) {
            mInstance = new GankManager();
        }
        return mInstance;
    }

    private GankManager() {
        // HttpClient添加拦截打印url
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                //打印url
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
                return response;
            }
        });
        // 初始化Retrofit，动态代理生成实现类
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_GANK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient.build())
                .build();

        mService = retrofit.create(GankService.class);
    }

    /**
     * 获取某天的数据
     */
    public Observable<GankDailyResult> getDaily(int year, int month, int day) {
        return mService.getDailyData(year, month, day);
    }

    public Observable<GankCategoryResult> getCategory(String type, int count, int page) {
        return mService.getCategoryData(type, count, page);
    }
}

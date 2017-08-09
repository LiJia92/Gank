package com.study.lijia.gank.net;

import retrofit2.Retrofit;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(GankService.class);
    }

    public GankService getGankService() {
        return mService;
    }
}

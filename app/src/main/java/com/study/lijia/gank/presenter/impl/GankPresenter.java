package com.study.lijia.gank.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.study.lijia.gank.data.DataModel;
import com.study.lijia.gank.net.GankTask;
import com.study.lijia.gank.presenter.AbsPresenter;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.view.IGankView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lijia on 17-8-9.
 */

public class GankPresenter extends AbsPresenter implements IGankPresenter {

    private IGankView mView;

    public GankPresenter(IGankView view) {
        super(view);
        mView = view;
    }

    @Override
    public void getDaily(String year, String month, String day) {
        Call<DataModel> model = GankTask.getInstance().getGankService().getDailyData(year, month, day);
        model.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(@Nullable Call<DataModel> call, @NonNull Response<DataModel> response) {
                if (mView != null) {
                    DataModel dataModel = response.body();
                    String str = new Gson().toJson(dataModel);
                    mView.showDaily(str);
                }
            }

            @Override
            public void onFailure(@Nullable Call<DataModel> call, @NonNull Throwable t) {
                if (mView != null) {
                    mView.onError(t.getMessage());
                }
            }
        });
    }

    @Override
    public void getTypeData(String type) {

    }
}

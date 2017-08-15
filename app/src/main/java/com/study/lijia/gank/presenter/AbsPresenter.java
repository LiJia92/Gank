package com.study.lijia.gank.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.study.lijia.gank.view.IView;

/**
 * Presenter基类
 * Created by lijia on 17-8-9.
 */

public abstract class AbsPresenter implements IPresenter {

    private Context mContext;
    private AppCompatActivity mActivity;

    public AbsPresenter(IView view) {
        if (view instanceof AppCompatActivity) {
            initWithActivity((AppCompatActivity) view);
        } else if (view instanceof Fragment) {
            initWithFragment((Fragment) view);
        } else {
            throw new RuntimeException("view must be instance of AppCompatActivity or Fragment");
        }
    }

    private void initWithActivity(AppCompatActivity activity) {
        mActivity = activity;
        mContext = mActivity;
    }

    private void initWithFragment(Fragment fragment) {
        mActivity = (AppCompatActivity) fragment.getActivity();
        mContext = fragment.getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroyView() {
        mActivity = null;
        mContext = null;
    }
}

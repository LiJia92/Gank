package com.study.lijia.gank.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 基类
 * Created by lijia on 17-8-16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        findWidgets();

        initData();

        initWidgets();

        setListener();
    }

    protected abstract int getLayoutResId();

    /**
     * 引用ButterKnife可省略这个方法，空实现，子类自由覆盖
     */
    protected void findWidgets() {

    }

    protected abstract void initWidgets();

    protected abstract void setListener();

    protected void initData() {

    }
}

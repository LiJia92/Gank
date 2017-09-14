package com.study.lijia.gank.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.study.lijia.gank.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 含有 AppBar 的 Activity 基类
 * Created by lijia on 17-8-16.
 */

public abstract class BaseAppBarActivity extends AppCompatActivity {

    protected Context mContext = this;

    protected ActionBar mActionBar;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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

    @CallSuper
    protected void initWidgets() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected abstract void setListener();

    protected void initData() {

    }

    protected void setTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

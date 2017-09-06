package com.study.lijia.gank.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.ToastUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankResult;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.ui.adapter.GankListAdapter;
import com.study.lijia.gank.view.IGankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GankListAppBarActivity extends BaseAppBarActivity implements IGankView {

    private IGankPresenter mGankPresenter;

    private GankListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.gank_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mGankPresenter = new GankPresenter(this);
        mGankPresenter.loadMoreDaily();
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mRefreshLayout.setRefreshing(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new GankListAdapter(null);
        mAdapter.openLoadAnimation(new SlideInRightAnimation());
        mAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(mAdapter);

        // 抽屉菜单填充内容
        mNavigationView.inflateHeaderView(R.layout.header_navigation);
        mNavigationView.inflateMenu(R.menu.menu_navigation);

        // ActionBar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankPresenter.loadMoreDaily();
                mRefreshLayout.setEnabled(false);
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mGankPresenter.getDailyDetail((GankResult) adapter.getItem(position));
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGankPresenter.refreshDaily();
                mAdapter.setEnableLoadMore(false);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(String msg) {
        ToastUtils.showShort(mContext, msg);
    }

    @Override
    public void showDaily(List<GankResult> gankResults) {
        if (mRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(gankResults);
            mRefreshLayout.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
        } else {
            mAdapter.loadMoreComplete();
            mAdapter.addData(gankResults);
            mRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void showDailyData(ArrayList<List<GankBaseData>> dailyData) {
        GankDetailAppBarActivity.navigateTo(mContext, dailyData);
    }
}

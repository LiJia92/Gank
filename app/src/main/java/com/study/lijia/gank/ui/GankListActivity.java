package com.study.lijia.gank.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import com.study.lijia.gank.data.GankDailyResult;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.ui.adapter.GankCategoryAdapter;
import com.study.lijia.gank.ui.adapter.GankDailyAdapter;
import com.study.lijia.gank.view.IGankView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GankListActivity extends BaseAppBarActivity implements IGankView {

    @BindView(R.id.gank_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    private GankDailyAdapter mDailyAdapter;
    private GankCategoryAdapter mCategoryAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private IGankPresenter mGankPresenter;
    private int mOldMenu;
    private String mCurrentType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mOldMenu = R.id.navigation_daily;
        mCurrentType = mContext.getResources().getString(R.string.daily);
        mGankPresenter = new GankPresenter(this);
        mGankPresenter.loadMoreDaily();
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mRefreshLayout.setRefreshing(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDailyAdapter = new GankDailyAdapter(null);
        mDailyAdapter.openLoadAnimation(new SlideInRightAnimation());
        mDailyAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(mDailyAdapter);

        mCategoryAdapter = new GankCategoryAdapter(null);
        mCategoryAdapter.openLoadAnimation(new SlideInRightAnimation());
        mCategoryAdapter.setPreLoadNumber(3);

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
        mDailyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankPresenter.loadMoreDaily();
                mRefreshLayout.setEnabled(false);
            }
        }, mRecyclerView);

        mDailyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mGankPresenter.getDailyDetail((GankDailyResult) adapter.getItem(position));
            }
        });

        mCategoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankPresenter.loadMoreCategory(mCurrentType);
                mRefreshLayout.setEnabled(false);
            }
        }, mRecyclerView);

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GankBaseData data = (GankBaseData) adapter.getItem(position);
                if (data != null) {
                    GankWebActivity.navigateTo(mContext, data.desc, data.url);
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGankPresenter.refreshData(mCurrentType);
                if (mRecyclerView.getAdapter() instanceof BaseQuickAdapter) {
                    ((BaseQuickAdapter) mRecyclerView.getAdapter()).setEnableLoadMore(false);
                }
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id != mOldMenu) {
                    item.setChecked(true);
                    MenuItem item1 = mNavigationView.getMenu().findItem(mOldMenu);
                    if (item1 != null) {
                        item1.setChecked(false);
                    }
                    mOldMenu = id;
                    mCurrentType = item.getTitle().toString();
                    setTitle(mCurrentType);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    mRefreshLayout.setRefreshing(true);
                    mGankPresenter.refreshData(mCurrentType);
                }
                return true;
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
    public void showDaily(List<GankDailyResult> gankDailyResults) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            mDailyAdapter.setEnableLoadMore(true);
            mDailyAdapter.setNewData(gankDailyResults);
        } else {
            mRefreshLayout.setEnabled(true);
            mDailyAdapter.loadMoreComplete();
            mDailyAdapter.addData(gankDailyResults);
        }
        if (!(mRecyclerView.getAdapter() instanceof GankDailyAdapter)) {
            mRecyclerView.setAdapter(mDailyAdapter);
        }
    }

    @Override
    public void showDailyData(ArrayList<List<GankBaseData>> dailyData) {
        GankDetailActivity.navigateTo(mContext, dailyData);
    }

    @Override
    public void showCategoryData(List<GankBaseData> categoryDataList) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            mCategoryAdapter.setEnableLoadMore(true);
            mCategoryAdapter.setNewData(categoryDataList);
        } else {
            mRefreshLayout.setEnabled(true);
            mCategoryAdapter.loadMoreComplete();
            mCategoryAdapter.addData(categoryDataList);
        }
        if (!(mRecyclerView.getAdapter() instanceof GankCategoryAdapter)) {
            mRecyclerView.setAdapter(mCategoryAdapter);
        }
    }
}

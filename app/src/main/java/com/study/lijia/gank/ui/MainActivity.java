package com.study.lijia.gank.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.ToastUtils;
import com.study.lijia.gank.data.DataModel;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.ui.adapter.MainAdapter;
import com.study.lijia.gank.view.IGankView;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IGankView {

    private IGankPresenter mGankPresenter;

    private MainAdapter mAdapter;

    @BindView(R.id.gank_rv)
    RecyclerView mRv;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout mRefreshLayout;

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
        mRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setRefreshing(true);

        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MainAdapter(null);
        mAdapter.openLoadAnimation(new SlideInRightAnimation());
        mAdapter.setPreLoadNumber(3);
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankPresenter.loadMoreDaily();
                mRefreshLayout.setEnabled(false);
            }
        }, mRv);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(String msg) {
        ToastUtils.showShort(mContext, msg);
    }

    @Override
    public void showDaily(List<DataModel> dataModels) {
        if (mRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(dataModels);
            mRefreshLayout.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
        } else {
            mAdapter.loadMoreComplete();
            mAdapter.addData(dataModels);
            mRefreshLayout.setEnabled(true);
        }
    }
}

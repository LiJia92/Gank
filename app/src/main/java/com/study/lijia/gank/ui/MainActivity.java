package com.study.lijia.gank.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.study.lijia.gank.R;
import com.study.lijia.gank.data.DataModel;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.ui.adapter.MainAdapter;
import com.study.lijia.gank.view.IGankView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IGankView {

    private IGankPresenter mGankPresenter;

    private MainAdapter mAdapter;

    @BindView(R.id.gank_rv)
    RecyclerView mRv;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.addLogAdapter(new AndroidLogAdapter());

        mRefreshLayout.setRefreshing(true);
        mGankPresenter = new GankPresenter(this);
        mGankPresenter.loadMoreDaily();

        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new MainAdapter(null);
        mAdapter.openLoadAnimation(new SlideInRightAnimation());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mGankPresenter.loadMoreDaily();
                mRefreshLayout.setEnabled(false);
            }
        }, mRv);
        mAdapter.setPreLoadNumber(3);
        mRv.setAdapter(mAdapter);

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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

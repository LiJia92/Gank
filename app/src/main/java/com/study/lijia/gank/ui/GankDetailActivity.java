package com.study.lijia.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.study.lijia.gank.R;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.ui.adapter.DetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 一天的详情页面
 * Created by lijia on 17-8-16.
 */

public class GankDetailActivity extends BaseActivity {

    private static final String EXTRA_DATA = "extraData";

    @BindView(R.id.detail_rv)
    RecyclerView mRecyclerView;

    private DetailAdapter mAdapter;
    private ArrayList<List<GankBaseData>> mData;

    public static void navigateTo(Context context, ArrayList<List<GankBaseData>> result) {
        Intent intent = new Intent(context, GankDetailActivity.class);
        intent.putExtra(EXTRA_DATA, result);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail_gank;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        mData = (ArrayList<List<GankBaseData>>) getIntent().getSerializableExtra(EXTRA_DATA);
    }

    @Override
    protected void initWidgets() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DetailAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

    }
}



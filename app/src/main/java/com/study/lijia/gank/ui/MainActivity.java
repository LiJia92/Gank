package com.study.lijia.gank.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.addLogAdapter(new AndroidLogAdapter());

        mGankPresenter = new GankPresenter(this);
        mGankPresenter.getDaily();

        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
        mAdapter = new MainAdapter(dataModels);
        mAdapter.openLoadAnimation(new SlideInRightAnimation());
        mRv.setAdapter(mAdapter);
    }
}

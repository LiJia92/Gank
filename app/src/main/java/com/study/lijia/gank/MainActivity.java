package com.study.lijia.gank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.view.IGankView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IGankView {

    @BindView(R.id.content_text_view)
    TextView mContent;

    private IGankPresenter mGankPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGankPresenter = new GankPresenter(this);
        mGankPresenter.getDaily("2017", "08", "03");
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
    public void showDaily(String daily) {
        mContent.setText(daily);
    }
}

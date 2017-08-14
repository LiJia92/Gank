package com.study.lijia.gank.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.study.lijia.gank.R;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.view.IGankView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IGankView {

    @BindView(R.id.content_text_view)
    TextView mContent;

    @BindView(R.id.show_image)
    ImageView mImageView;

    private IGankPresenter mGankPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.addLogAdapter(new AndroidLogAdapter());

        mGankPresenter = new GankPresenter(this);
        mGankPresenter.getDaily();
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

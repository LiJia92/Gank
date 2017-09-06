package com.study.lijia.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

import com.study.lijia.gank.R;

import butterknife.BindView;

public class GankWebAppBarActivity extends BaseAppBarActivity {

    private final static String EXTRA_DESC = "extraDesc";
    private final static String EXTRA_URL = "extraUrl";

    public static void navigateTo(Context context, String desc, String url) {
        Intent intent = new Intent(context, GankWebAppBarActivity.class);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @BindView(R.id.gank_web)
    WebView mWebView;

    private String mDesc = "";
    private String mUrl = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gank_web;
    }

    @Override
    protected void initData() {
        mDesc = getIntent().getStringExtra(EXTRA_DESC);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void setListener() {

    }
}

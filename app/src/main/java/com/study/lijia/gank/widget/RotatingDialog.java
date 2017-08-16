package com.study.lijia.gank.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.DensityUtils;

/**
 * 带旋转的dialog
 */
public class RotatingDialog extends Dialog {

    private static final float NORMAL_HEIGHT = 82;
    private static final float NORMAL_WIDTH = 130;

    private ImageView mLoadingIv;
    private TextView mLoadingTv;
    private Animation mAnimation;
    private Context mContext;

    public RotatingDialog(Context context) {
        this(context, 0);
    }

    public RotatingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mLoadingIv = (ImageView) view.findViewById(R.id.dialog_loading_icon);
        mLoadingTv = (TextView) view.findViewById(R.id.dialog_loading_tv);

        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotating_clockwise);

        setContentView(view);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(DensityUtils.dip2px(context, NORMAL_WIDTH), DensityUtils.dip2px(context, NORMAL_HEIGHT));
        }
    }

    public void enlarge() {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(DensityUtils.dip2px(mContext, NORMAL_WIDTH * 1.5f), DensityUtils.dip2px(mContext, NORMAL_HEIGHT * 1.5f));
        }
    }

    public void setText(String text) {
        mLoadingTv.setText(text);
    }

    public void show() {
        mLoadingIv.startAnimation(mAnimation);
        super.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoadingIv.clearAnimation();
    }
}

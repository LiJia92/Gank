package com.study.lijia.gank.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.DensityUtils;
import com.study.lijia.gank.Utils.ImageUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.ui.GankWebActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页Adapter
 * Created by lijia on 17-8-16.
 */

public class GankDetailAdapter extends BaseQuickAdapter<List<GankBaseData>, GankDetailAdapter.GankViewHolder> {

    private int mDividerColor;
    private int mPadding;
    private ColorStateList mColorStateList;
    private Context mContext;

    public GankDetailAdapter(Context context, @Nullable ArrayList<List<GankBaseData>> data) {
        super(R.layout.item_daily_detail, data);
        mContext = context;
        mDividerColor = ContextCompat.getColor(mContext, R.color.divider);
        mPadding = DensityUtils.dip2px(mContext, 8);

        int[][] states = new int[][]{new int[]{}};
        mColorStateList = new ColorStateList(states, new int[]{ContextCompat.getColor(mContext, R.color.gray)});
    }

    @Override
    protected void convert(GankViewHolder helper, List<GankBaseData> item) {
        helper.rootLayout.removeAllViews();
        for (int i = 0; i < item.size(); i++) {
            GankBaseData data = item.get(i);
            if (i == 0) {
                addHeaderView(helper.rootLayout, data.type);
                addDivider(helper.rootLayout);
            }

            if (data.type.equals("福利")) {
                addPicture(helper.rootLayout, data.url);
            } else {
                addData(helper.rootLayout, data);
            }
        }
    }

    /**
     * 添加分类标题
     */
    private void addHeaderView(LinearLayout rootLayout, String type) {
        TextView header = new TextView(mContext);
        header.setText(type);
        int color;
        switch (type) {
            case "福利":
                color = ContextCompat.getColor(mContext, R.color.category_welfare);
                break;
            case "Android":
                color = ContextCompat.getColor(mContext, R.color.category_android);
                break;
            case "iOS":
                color = ContextCompat.getColor(mContext, R.color.category_iOS);
                break;
            case "前端":
                color = ContextCompat.getColor(mContext, R.color.category_js);
                break;
            case "App":
                color = ContextCompat.getColor(mContext, R.color.category_app);
                break;
            case "瞎推荐":
                color = ContextCompat.getColor(mContext, R.color.category_recommend);
                break;
            case "拓展资源":
                color = ContextCompat.getColor(mContext, R.color.category_resources);
                break;
            case "休息视频":
                color = ContextCompat.getColor(mContext, R.color.category_rest);
                break;
            default:
                color = ContextCompat.getColor(mContext, R.color.black);
                break;
        }
        header.setTextColor(color);
        header.setTextSize(20);
        header.getPaint().setFakeBoldText(true);
        header.setPadding(mPadding, mPadding, mPadding, mPadding);

        rootLayout.addView(header);
    }

    /**
     * 添加分割线
     */
    private void addDivider(LinearLayout rootLayout) {
        View divider = new View(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        divider.setLayoutParams(params);
        divider.setBackgroundColor(mDividerColor);
        rootLayout.addView(divider);
    }

    /**
     * 添加数据TextView
     */
    private void addData(LinearLayout rootLayout, final GankBaseData data) {
        TextView dataText = (TextView) View.inflate(mContext, R.layout.item_gank_data, null);
        dataText.setPadding(mPadding, mPadding, mPadding, mPadding);
        String content = data.desc.trim() + "   " + String.format("via.%s", data.who);
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        int start = content.indexOf("via");
        int end = content.length();
        ssb.setSpan(new TextAppearanceSpan("serif", Typeface.ITALIC, DensityUtils.sp2px(mContext, 12), mColorStateList, mColorStateList), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        dataText.setText(ssb);
        rootLayout.addView(dataText);

        dataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GankWebActivity.navigateTo(mContext, data.desc, data.url);
            }
        });
    }

    /**
     * 添加图片
     */
    private void addPicture(LinearLayout rootLayout, String url) {
        ImageView picture = new ImageView(mContext);
        picture.setPadding(mPadding, mPadding, mPadding, mPadding);
        picture.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageUtils.loadImage(mContext, url, picture);
        rootLayout.addView(picture);
    }

    class GankViewHolder extends BaseViewHolder {

        LinearLayout rootLayout;

        public GankViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.daily_detail_ll);
        }
    }
}


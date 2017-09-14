package com.study.lijia.gank.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.ImageUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankDailyResult;

import java.util.List;

/**
 * 每日精彩的Adapter
 * Created by lijia on 17-8-15.
 */

public class GankDailyAdapter extends BaseQuickAdapter<GankDailyResult, BaseViewHolder> {

    public GankDailyAdapter(@Nullable List<GankDailyResult> data) {
        super(R.layout.item_gank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankDailyResult item) {
        String date = "";
        if (item.results.welfareList != null
                && item.results.welfareList.size() > 0) {
            String url = item.results.welfareList.get(0).url;
            date = item.results.welfareList.get(0).publishedAt.substring(0, 10);
            ImageUtils.loadImage(mContext, url, (ImageView) helper.getView(R.id.gank_picture_iv));
        }

        if (item.results.restList != null && item.results.restList.size() > 0) {
            GankBaseData restData = item.results.restList.get(0);
            String title = restData.desc;
            String time = restData.publishedAt.substring(0, 10);

            helper.setText(R.id.gank_title_tv, title);
            helper.setText(R.id.gank_date_tv, time);
        } else {
            String title = "今天没有休息视频哟，继续搬砖吧～";
            helper.setText(R.id.gank_title_tv, title);
            helper.setText(R.id.gank_date_tv, date);
        }
    }
}

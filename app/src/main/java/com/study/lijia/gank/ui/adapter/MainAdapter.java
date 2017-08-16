package com.study.lijia.gank.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.ImageUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankResult;

import java.util.List;

/**
 * 首页列表的Adapter
 * Created by lijia on 17-8-15.
 */

public class MainAdapter extends BaseQuickAdapter<GankResult, BaseViewHolder> {

    public MainAdapter(@Nullable List<GankResult> data) {
        super(R.layout.item_gank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankResult item) {
        if (item.results.restList != null
                && item.results.restList.size() > 0
                && item.results.welfareList != null
                && item.results.welfareList.size() > 0) {
            GankBaseData restData = item.results.restList.get(0);
            String title = restData.desc;
            String time = restData.publishedAt.substring(0, 10);
            String url = item.results.welfareList.get(0).url;

            helper.setText(R.id.gank_title_tv, title);
            helper.setText(R.id.gank_date_tv, time);
            ImageUtils.loadImage(mContext, url, (ImageView) helper.getView(R.id.gank_picture_iv));
        }
    }
}

package com.study.lijia.gank.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.study.lijia.gank.R;
import com.study.lijia.gank.data.GankBaseData;

import java.util.List;

/**
 * 分类Adapter
 * Created by lijia on 17-9-14.
 */

public class GankCategoryAdapter extends BaseQuickAdapter<GankBaseData, BaseViewHolder> {

    public GankCategoryAdapter(@Nullable List<GankBaseData> data) {
        super(R.layout.item_category, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GankBaseData item) {
        helper.setText(R.id.category_title_tv, item.desc);
    }
}

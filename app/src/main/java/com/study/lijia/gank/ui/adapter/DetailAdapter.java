package com.study.lijia.gank.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.study.lijia.gank.R;
import com.study.lijia.gank.data.GankBaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页Adapter
 * Created by lijia on 17-8-16.
 */

public class DetailAdapter extends BaseQuickAdapter<List<GankBaseData>, DetailAdapter.ViewHolder> {

    public DetailAdapter(@Nullable ArrayList<List<GankBaseData>> data) {
        super(R.layout.item_daily_detail, data);
    }

    @Override
    protected void convert(ViewHolder helper, List<GankBaseData> item) {
        for (int i = 0; i < item.size(); i++) {
            GankBaseData data = item.get(i);
            if (i == 0) {
                TextView header = new TextView(mContext);
                header.setText(data.type);
                helper.rootLayout.addView(header);
            }

            if (data.type.equals("福利")) {

            } else {
                TextView header = new TextView(mContext);
                header.setText(data.desc);
                helper.rootLayout.addView(header);
            }
        }
    }

    public class ViewHolder extends BaseViewHolder {

        public LinearLayout rootLayout;

        public ViewHolder(View view) {
            super(view);
            rootLayout = (LinearLayout) view.findViewById(R.id.daily_detail_ll);
        }
    }
}


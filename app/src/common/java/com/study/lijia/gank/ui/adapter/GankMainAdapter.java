package com.study.lijia.gank.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.study.lijia.gank.GlideApp;
import com.study.lijia.gank.R;
import com.study.lijia.gank.Utils.DensityUtils;
import com.study.lijia.gank.Utils.ImageUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankDailyResult;

import java.util.List;

/**
 * 首页列表数据Adapter
 * Created by lijia on 17-8-15.
 */

public class GankMainAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private final static int TYPE_DAILY = 0;    // 每日精彩
    private final static int TYPE_WELFARE = 1;  // 福利
    private final static int TYPE_OTHERS = 2;   // 其他

    public GankMainAdapter(final Context context, @Nullable List<Object> dataList) {
        super(dataList);

        setMultiTypeDelegate(new MultiTypeDelegate<Object>() {
            @Override
            protected int getItemType(Object o) {
                if (o instanceof GankBaseData) {
                    GankBaseData data = (GankBaseData) o;
                    if (data.type.equals(context.getString(R.string.welfare))) {
                        return TYPE_WELFARE;
                    } else {
                        return TYPE_OTHERS;
                    }
                } else {
                    return TYPE_DAILY;
                }
            }
        });

        getMultiTypeDelegate()
                .registerItemType(TYPE_DAILY, R.layout.item_gank)
                .registerItemType(TYPE_WELFARE, R.layout.item_welfare)
                .registerItemType(TYPE_OTHERS, R.layout.item_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object object) {
        switch (helper.getItemViewType()) {
            case TYPE_DAILY:
                GankDailyResult item = (GankDailyResult) object;
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
                    String title = mContext.getString(R.string.empty_desc);
                    helper.setText(R.id.gank_title_tv, title);
                    helper.setText(R.id.gank_date_tv, date);
                }
                break;
            case TYPE_WELFARE:
                GankBaseData welfareItem = (GankBaseData) object;
                final String url = welfareItem.url;
                final ImageView imageView = helper.getView(R.id.category_welfare_iv);

                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = (int) ((Math.random() + 1) * DensityUtils.dip2px(mContext, 200));

                GlideApp.with(mContext)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.img_default_gray)
                        .into(imageView);
                break;
            case TYPE_OTHERS:
                GankBaseData otherItem = (GankBaseData) object;
                helper.setText(R.id.category_title_tv, otherItem.desc);
                helper.setText(R.id.category_writer_tv, "via." + otherItem.who);
                helper.setText(R.id.category_date_tv, otherItem.publishedAt.substring(0, 10));
                break;
        }
    }

}

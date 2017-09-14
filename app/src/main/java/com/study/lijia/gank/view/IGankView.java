package com.study.lijia.gank.view;

import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankDailyResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 对应IGankPresenter，展示数据
 * Created by lijia on 17-8-9.
 */

public interface IGankView extends IView {
    /**
     * 展示首页列表数据
     */
    void showDaily(List<GankDailyResult> gankDailyResults);

    /**
     * 展示一天的详情数据
     */
    void showDailyData(ArrayList<List<GankBaseData>> dailyData);

    void showCategoryData(List<GankBaseData> categoryDataList);
}

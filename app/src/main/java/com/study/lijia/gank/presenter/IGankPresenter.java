package com.study.lijia.gank.presenter;

import com.study.lijia.gank.data.GankDailyResult;

/**
 * Gank请求接口
 * Created by lijia on 17-8-9.
 */

public interface IGankPresenter {

    /**
     * 刷新数据
     */
    void refreshData(String type);

    /**
     * 加载更多
     */
    void loadMoreDaily();

    /**
     * 获取每日详情
     */
    void getDailyDetail(GankDailyResult result);

    /**
     * 获取分类数据
     */
    void loadMoreCategory(String type);
}

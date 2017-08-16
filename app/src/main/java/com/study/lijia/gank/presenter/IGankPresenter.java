package com.study.lijia.gank.presenter;

import com.study.lijia.gank.data.GankResult;

/**
 * Gank请求接口
 * Created by lijia on 17-8-9.
 */

public interface IGankPresenter {

    void refreshDaily();

    void loadMoreDaily();

    void getDailyDetail(GankResult result);
}

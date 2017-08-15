package com.study.lijia.gank.view;

import com.study.lijia.gank.data.DataModel;

import java.util.List;

/**
 * 对应IGankPresenter，展示数据
 * Created by lijia on 17-8-9.
 */

public interface IGankView extends IView {
    /**
     * 展示首页列表数据
     */
    void showDaily(List<DataModel> dataModels);
}

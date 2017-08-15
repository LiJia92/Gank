package com.study.lijia.gank.presenter;

import android.content.Intent;

/**
 * Presenter最上层接口，抽取公用方法
 * Created by lijia on 17-8-9.
 */

public interface IPresenter {

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onDestroyView();
}

package com.study.lijia.gank.presenter.impl;

import com.orhanobut.logger.Logger;
import com.study.lijia.gank.Utils.DateUtils;
import com.study.lijia.gank.data.GankBaseData;
import com.study.lijia.gank.data.GankCategoryData;
import com.study.lijia.gank.data.GankResult;
import com.study.lijia.gank.net.GankManager;
import com.study.lijia.gank.presenter.AbsPresenter;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.view.IGankView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Gank接口实现类
 * Created by lijia on 17-8-9.
 */

public class GankPresenter extends AbsPresenter implements IGankPresenter {

    private final static int PAGE_SIZE = 20;    // 默认一次请求20天的数据

    private IGankView mView;

    private List<GankResult> mDataList = new ArrayList<>();
    private EasyDate mCurrentDate;
    private int mPage = 1;

    public GankPresenter(IGankView view) {
        super(view);
        long time = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCurrentDate = new EasyDate(mCalendar);
        mPage = 1;
        mView = view;
    }

    @Override
    public void refreshDaily() {
        mPage = 1;
        mDataList.clear();
        loadMoreDaily();
        Logger.e("refreshDaily");
    }

    @Override
    public void loadMoreDaily() {
        Logger.e("loadMoreDaily");
        Observable.just(mCurrentDate)
                .flatMapIterable(new Function<EasyDate, Iterable<EasyDate>>() {
                    @Override
                    public Iterable<EasyDate> apply(@NonNull EasyDate easyDate) throws Exception {
                        return easyDate.getPastTime();
                    }
                })
                .concatMap(new Function<EasyDate, ObservableSource<GankResult>>() {
                    @Override
                    public ObservableSource<GankResult> apply(@NonNull EasyDate easyDate) throws Exception {
                        return GankManager.getInstance().getDaily(easyDate.getYear(), easyDate.getMonth(), easyDate.getDay());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GankResult>() {
                    @Override
                    public void accept(GankResult gankResult) throws Exception {
                        if (gankResult.results.androidList != null && gankResult.results.androidList.size() != 0) {
                            mDataList.add(gankResult);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mPage++;
                        if (mView != null) {
                            List<GankResult> gankResults = new ArrayList<>();
                            gankResults.addAll(mDataList);
                            mDataList.clear();
                            mView.showDaily(gankResults);
                        }
                    }
                });
    }

    @Override
    public void getDailyDetail(GankResult result) {
        Observable.just(result.results)
                .map(new Function<GankCategoryData, ArrayList<List<GankBaseData>>>() {

                    @Override
                    public ArrayList<List<GankBaseData>> apply(@NonNull GankCategoryData gankCategoryData) throws Exception {
                        ArrayList<List<GankBaseData>> cardData = new ArrayList<>();
                        if (gankCategoryData.welfareList != null && gankCategoryData.welfareList.size() > 0) {
                            cardData.add(gankCategoryData.welfareList);
                        }
                        if (gankCategoryData.androidList != null && gankCategoryData.androidList.size() > 0) {
                            cardData.add(gankCategoryData.androidList);
                        }
                        if (gankCategoryData.iOSList != null && gankCategoryData.iOSList.size() > 0) {
                            cardData.add(gankCategoryData.iOSList);
                        }
                        if (gankCategoryData.jsList != null && gankCategoryData.jsList.size() > 0) {
                            cardData.add(gankCategoryData.jsList);
                        }
                        if (gankCategoryData.restList != null && gankCategoryData.restList.size() > 0) {
                            cardData.add(gankCategoryData.restList);
                        }
                        if (gankCategoryData.resourcesList != null && gankCategoryData.resourcesList.size() > 0) {
                            cardData.add(gankCategoryData.resourcesList);
                        }
                        if (gankCategoryData.appList != null && gankCategoryData.appList.size() > 0) {
                            cardData.add(gankCategoryData.appList);
                        }
                        if (gankCategoryData.recommendList != null && gankCategoryData.recommendList.size() > 0) {
                            cardData.add(gankCategoryData.recommendList);
                        }
                        return cardData;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<List<GankBaseData>>>() {
                    @Override
                    public void accept(ArrayList<List<GankBaseData>> lists) throws Exception {
                        if (mView != null) {
                            mView.showDailyData(lists);
                        }
                    }
                });
    }

    /**
     * 查每日干货需要的特殊的类
     */
    private class EasyDate implements Serializable {
        private Calendar calendar;

        EasyDate(Calendar calendar) {
            this.calendar = calendar;
        }

        int getYear() {
            return calendar.get(Calendar.YEAR);
        }

        int getMonth() {
            return calendar.get(Calendar.MONTH) + 1;
        }

        int getDay() {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        List<EasyDate> getPastTime() {
            List<EasyDate> easyDates = new ArrayList<>();
            for (int i = 0; i < PAGE_SIZE; i++) {
                long time = this.calendar.getTimeInMillis() - ((mPage - 1) * PAGE_SIZE * DateUtils.ONE_DAY) - i * DateUtils.ONE_DAY;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);

                // 过滤掉周末，减少无效请求
                if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    EasyDate date = new EasyDate(c);
                    easyDates.add(date);
                }
            }
            return easyDates;
        }
    }
}

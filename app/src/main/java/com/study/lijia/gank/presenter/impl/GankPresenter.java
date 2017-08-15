package com.study.lijia.gank.presenter.impl;

import com.orhanobut.logger.Logger;
import com.study.lijia.gank.Utils.DateUtils;
import com.study.lijia.gank.data.DataModel;
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

    private final static int PAGE_SIZE = 20;    // 默认一次请求30天的数据

    private IGankView mView;

    private List<DataModel> mDataList = new ArrayList<>();
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
                .concatMap(new Function<EasyDate, ObservableSource<DataModel>>() {
                    @Override
                    public ObservableSource<DataModel> apply(@NonNull EasyDate easyDate) throws Exception {
                        return GankManager.getInstance().getDaily(easyDate.getYear(), easyDate.getMonth(), easyDate.getDay());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataModel>() {
                    @Override
                    public void accept(DataModel dataModel) throws Exception {
                        if (dataModel.results.androidList != null && dataModel.results.androidList.size() != 0) {
                            mDataList.add(dataModel);
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
                            List<DataModel> dataModels = new ArrayList<>();
                            dataModels.addAll(mDataList);
                            mDataList.clear();
                            mView.showDaily(dataModels);
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
                EasyDate date = new EasyDate(c);
                easyDates.add(date);
            }
            return easyDates;
        }
    }
}

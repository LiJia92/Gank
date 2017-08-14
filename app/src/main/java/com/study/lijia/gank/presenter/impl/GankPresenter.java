package com.study.lijia.gank.presenter.impl;

import com.orhanobut.logger.Logger;
import com.study.lijia.gank.Utils.DateUtils;
import com.study.lijia.gank.data.DataModel;
import com.study.lijia.gank.net.GankTask;
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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lijia on 17-8-9.
 */

public class GankPresenter extends AbsPresenter implements IGankPresenter {

    private final static int PAGE_SIZE = 10;

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
    public void getDaily() {
        Observable.just(mCurrentDate)
                .flatMapIterable(new Function<EasyDate, Iterable<EasyDate>>() {
                    @Override
                    public Iterable<EasyDate> apply(@NonNull EasyDate easyDate) throws Exception {
                        return easyDate.getPastTime();
                    }
                })
                .flatMap(new Function<EasyDate, ObservableSource<DataModel>>() {
                    @Override
                    public ObservableSource<DataModel> apply(@NonNull EasyDate easyDate) throws Exception {
                        return GankTask.getInstance().getGankService().getDailyData(easyDate.getYear(), easyDate.getMonth(), easyDate.getDay());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataModel>() {
                    @Override
                    public void accept(DataModel dataModel) throws Exception {
                        mDataList.add(dataModel);
                        Logger.e("请求完成了");
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

        public List<EasyDate> getPastTime() {
            List<EasyDate> easyDates = new ArrayList<>();
            for (int i = 0; i < PAGE_SIZE; i++) {
                /*
                 * - (page * DateUtils.ONE_DAY) 翻到哪页再找 一页有DEFAULT_DAILY_SIZE这么长
                 * - i * DateUtils.ONE_DAY 往前一天一天 找呀找
                 */
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

package com.study.lijia.gank.net;

import com.study.lijia.gank.data.GankResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 每日数据： http://gank.io/api/day/年/月/日
 * 例：
 * http://gank.io/api/day/2015/08/06
 * <p>
 * <p>
 * <p>
 * Created by lijia on 17-8-7.
 */

public interface GankService {

    @GET("day/{year}/{month}/{day}")
    Observable<GankResult> getDailyData(@Path("year") int year, @Path("month") int month, @Path("day") int day);
}

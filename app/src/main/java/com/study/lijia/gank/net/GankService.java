package com.study.lijia.gank.net;

import com.study.lijia.gank.data.GankCategoryResult;
import com.study.lijia.gank.data.GankDailyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 每日数据： http://gank.io/api/day/年/月/日
 * 例：
 * http://gank.io/api/day/2015/08/06
 * <p>
 * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
 * 例：
 * http://gank.io/api/data/Android/10/1
 * <p>
 * <p>
 * <p>
 * Created by lijia on 17-8-7.
 */

public interface GankService {

    @GET("day/{year}/{month}/{day}")
    Observable<GankDailyResult> getDailyData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/{type}/{count}/{page}")
    Observable<GankCategoryResult> getCategoryData(@Path("type") String type, @Path("count") int count, @Path("page") int page);
}

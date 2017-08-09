package com.study.lijia.gank.net;

import com.study.lijia.gank.data.DataModel;

import retrofit2.Call;
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
    Call<DataModel> getDailyData(@Path("year") String year, @Path("month") String month, @Path("day") String day);
}

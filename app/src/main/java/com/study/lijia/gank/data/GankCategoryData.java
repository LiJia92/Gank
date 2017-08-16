package com.study.lijia.gank.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Gank分类数据
 * Created by lijia on 17-8-16.
 */

public class GankCategoryData implements Serializable {

    @SerializedName("Android")
    public ArrayList<GankBaseData> androidList;

    @SerializedName("iOS")
    public ArrayList<GankBaseData> iOSList;

    @SerializedName("休息视频")
    public ArrayList<GankBaseData> restList;

    @SerializedName("前端")
    public ArrayList<GankBaseData> jsList;

    @SerializedName("福利")
    public ArrayList<GankBaseData> welfareList;

    @SerializedName("瞎推荐")
    public ArrayList<GankBaseData> recommendList;

    @SerializedName("拓展资源")
    public ArrayList<GankBaseData> resourcesList;

    @SerializedName("App")
    public ArrayList<GankBaseData> appList;
}

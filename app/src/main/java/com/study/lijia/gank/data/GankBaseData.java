package com.study.lijia.gank.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Gank基本数据
 * Created by lijia on 17-8-16.
 */

public class GankBaseData implements Serializable {
    @SerializedName("_id")
    public String id;
    public String createdAt;
    public String desc;
    public String[] images;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public String used;
    public String who;
}

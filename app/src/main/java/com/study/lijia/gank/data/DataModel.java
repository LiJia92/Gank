package com.study.lijia.gank.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 数据结构类
 * Created by lijia on 17-8-9.
 */

public class DataModel implements Serializable {

    public String error;
    public DataResults results;
    public List<String> category;

    public class DataResults {
        @SerializedName("Android")
        public List<ItemData> androidList;

        @SerializedName("iOS")
        public List<ItemData> iOSList;

        @SerializedName("休息视频")
        public List<ItemData> restList;

        @SerializedName("前端")
        public List<ItemData> jsList;

        @SerializedName("福利")
        public List<ItemData> welfareList;
    }

    public class ItemData {
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

}


package com.study.lijia.gank.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijia on 17-8-9.
 */

public class DataModel implements Serializable {

    public String error;
    public DataResults results;


    public class DataResults {
        @SerializedName("Android")
        List<ItemData> androidList;

        @SerializedName("iOS")
        List<ItemData> iOSList;

        @SerializedName("休息视频")
        List<ItemData> restList;

        @SerializedName("前端")
        List<ItemData> jsList;

        @SerializedName("福利")
        List<ItemData> welfareList;
    }


    public class ItemData {
        public String _id;
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


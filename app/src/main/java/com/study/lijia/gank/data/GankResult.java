package com.study.lijia.gank.data;

import java.io.Serializable;
import java.util.List;

/**
 * Gank返回结构类
 * Created by lijia on 17-8-9.
 */

public class GankResult implements Serializable{

    public String error;
    public GankCategoryData results;
    public List<String> category;
}


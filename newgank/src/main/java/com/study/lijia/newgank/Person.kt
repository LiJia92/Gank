package com.study.lijia.newgank

import android.util.Log

/**
 * 简单实体
 * Created by lijia on 17-11-7.
 */
class Person(name: String, age: Int) {
    init {
        Log.e("TAG", name + age)
    }
}
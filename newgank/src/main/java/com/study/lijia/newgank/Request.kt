package com.study.lijia.newgank

import android.util.Log
import java.net.URL

/**
 * 网络请求
 * Created by lijia on 17-11-7.
 */
class Request(private val url: String) {
    fun run() {
        val forecastJsonStr = URL(url).readText()
        Log.d(javaClass.simpleName, forecastJsonStr)
    }
}
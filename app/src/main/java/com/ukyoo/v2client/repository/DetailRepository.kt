package com.ukyoo.v2client.repository

import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import javax.inject.Inject
import javax.inject.Named

/**
 * 主题详情
 */
class DetailRepository{

    @Inject
    @Named("non_cached")
    lateinit var apiService: HtmlService  //需要解析html的请求
    @Inject
    lateinit var jsonService: JsonService //返回json的请求









}


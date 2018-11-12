package com.ukyoo.v2cliecnt.api

import com.ukyoo.v2cliecnt.entity.TopicEntity
import io.reactivex.Flowable
import retrofit2.http.GET


interface ApiService {

    //最热主题  topics/hot.json
    @GET("/topics/latest.json")
    fun queryHotTopics(): Flowable<List<TopicEntity>>


    //最新主题  topics/latest.json
    @GET("/topics/hot.json")
    fun queryLatestTopics(): Flowable<List<TopicEntity>>

    //主题
    fun queryTopics(): Flowable<Object>

    //所有节点
    @GET("/nodes/all.json")
    fun getAllNodes(): Flowable<Object>

    //节点信息  nodes/show.json
    @GET("nodes/show.json")
    fun getNodesInfo(): Flowable<Object>

    //用户主页  members/show.json
    @GET("members/show.json")
    fun getUserInfo(): Flowable<Object>

    //所有回复  /replies/show.json
    @GET("/replies/show.json")
    fun getAllReplies(): Flowable<Object>

}

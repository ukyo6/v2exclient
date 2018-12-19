package com.ukyoo.v2client.api

import com.ukyoo.v2client.entity.TopicModel
import io.reactivex.Single
import retrofit2.http.*
import io.reactivex.Flowable
import retrofit2.http.POST


interface HtmlService {

    //最热主题  topics/hot.json
    @GET("/topics/latest.json")
    fun queryHotTopics(): Single<List<TopicModel>>

    //最新主题  topics/latest.json
    @GET("/topics/hot.json")
    fun queryLatestTopics(): Single<List<TopicModel>>

    //主题  (技术,创意,好玩...)
    @Headers("Referer: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @GET("/")
    fun queryTopicsByTab(@Query("tab") nodesId: String): Single<String>

    //所有节点
    @GET("/nodes/all.json")
    fun getAllNodes(): Single<Any>

    //节点信息  nodes/show.json
    @GET("nodes/show.json")
    fun getNodesInfo(): Single<Any>

    //用户主页  members/show.json
    @GET("members/show.json")
    fun getUserInfo(): Single<Any>

    //查看一个帖子的主题和回复
    @Headers("Referer: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @GET("t/{topicId}")
    fun getTopicAndRepliesByTopicId(@Path("topicId") topicId: Int, @Query("p") page: Int): Single<String>


}

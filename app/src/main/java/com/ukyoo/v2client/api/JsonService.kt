package com.ukyoo.v2client.api

import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.TopicModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonService {

    //最热主题  topics/hot.json
    @GET("/topics/latest.json")
    fun queryHotTopics(): Single<List<TopicModel>>

    //最新主题  topics/latest.json
    @GET("/topics/hot.json")
    fun queryLatestTopics(): Single<List<TopicModel>>

    //根据名字查找主题列表
    @GET("/api/topics/show.json")
    fun queryTopicsByName(@Query("node_name") name: String): Single<java.util.ArrayList<TopicModel>>

    //所有节点
    @GET("/api/nodes/all.json")
    fun getAllNodes(): Single<ArrayList<NodeModel>>

    //节点信息  nodes/show.json
    @GET("nodes/show.json")
    fun getNodesInfo(): Single<Any>

    //用户主页  members/show.json
    @GET("members/show.json")
    fun getUserInfo(): Single<Any>

    //所有回复  /replies/show.json
    @GET("/replies/show.json")
    fun getAllReplies(): Single<Any>

}

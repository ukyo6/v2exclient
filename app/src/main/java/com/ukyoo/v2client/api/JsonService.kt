package com.ukyoo.v2client.api

import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonService {

    //最热主题
    @GET("/topics/latest.json")
    fun queryHotTopics(): Single<List<TopicModel>>

    //最新主题
    @GET("/topics/hot.json")
    fun queryLatestTopics(): Single<List<TopicModel>>

    //根据名字查找主题列表
    @GET("/api/topics/show.json")
    fun queryTopicsByName(@Query("node_name") name: String): Single<ArrayList<TopicModel>>

    //所有节点
    @GET("/api/nodes/all.json")
    fun getAllNodes(): Single<ArrayList<NodeModel>>

    //节点信息
    @GET("nodes/show.json")
    fun getNodesInfo(): Single<Any>

    //用户主页
    @GET("members/show.json")
    fun getUserInfo(): Single<Any>

    //所有回复
    @GET("/api/replies/show.json")
    fun getRepliesByTopicId(@Query("topic_id") name: String): Single<ArrayList<ReplyModel>>

}

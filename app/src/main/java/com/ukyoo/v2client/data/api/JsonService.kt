package com.ukyoo.v2client.data.api

import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.entity.MemberInfo
import com.ukyoo.v2client.entity.TopicModelNew
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
    fun queryTopicsByName(@Query("node_name") name: String): Single<ArrayList<TopicModelNew>>

    //所有节点
    @GET("/api/nodes/all.json")
    fun getAllNodes(): Single<ArrayList<NodeModel>>

    //节点信息
    @GET("nodes/show.json")
    fun getNodesInfo(): Single<Any>

    //用户主页
    @GET("/api/members/show.json")
    fun getUserInfo(@Query("username") username: String): Single<MemberInfo>

    //查询帖子内容
    @GET("/api/topics/show.json")
    fun getTopicByTopicId(@Query("id") name: Int): Single<ArrayList<TopicModel>>

    //所有回复
    @GET("/api/replies/show.json")
    fun getRepliesByTopicId(@Query("topic_id") name: Int): Single<ArrayList<ReplyModel>>

}

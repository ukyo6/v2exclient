package com.ukyoo.v2client.data.api

import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.data.entity.ReplyItem
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.data.entity.UserInfoModel
import io.reactivex.Flowable
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
    fun queryTopicsByName(@Query("node_name") name: String): Flowable<ArrayList<TopicModel>>

    //所有节点 -
    @GET("/api/nodes/all.json")
    fun getAllNodes(): Flowable<ArrayList<NodeModel>>

    //节点信息
    @GET("nodes/show.json")
    fun getNodesInfo(): Single<Any>

    //用户主页 -
    @GET("/api/members/show.json")
    fun getUserInfo(@Query("username") username: String): Flowable<UserInfoModel>


    //查询主题信息
    @GET("/api/topics/show.json")
    fun getTopicByTopicId(@Query("id") name: Int): Single<ArrayList<TopicModel>>

    //所有回复
    @GET("/api/replies/show.json")
    fun getRepliesByTopicId(@Query("topic_id") name: Int): Single<ArrayList<ReplyItem>>

}

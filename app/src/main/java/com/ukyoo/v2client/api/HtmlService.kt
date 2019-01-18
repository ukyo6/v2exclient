package com.ukyoo.v2client.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*


interface HtmlService {

    //主题  (技术,创意,好玩...)
    @Headers("Referer: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @GET("/")
    fun queryTopicsByTab(@Query("tab") nodesId: String): Single<String>

    //查看一个帖子的主题和回复
    @Headers("Referer: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @GET("t/{topicId}")
    fun getTopicAndRepliesByTopicId(@Path("topicId") topicId: Int, @Query("p") page: Int): Single<String>

    //登录首页信息
//    @Headers(
////        "X-Requested-With: com.android.browser",
////        "User-Agent: Mozilla/5.0 (Linux; U; Android 4.2.1; en-us; M040 Build/JOP40D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
////    )
    @GET("/signin")
    fun signin(): Single<Response<String>>

    //登录
//    @Headers(
//        "X-Requested-With: com.android.browser",
//        "User-Agent: Mozilla/5.0 (Linux; U; Android 4.2.1; en-us; M040 Build/JOP40D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
//    )
    @POST("/signin")
    fun login(@HeaderMap headers: Map<String, String>, @QueryMap params: Map<String, String>): Single<String>

    //获取用户的主题
    @Headers("Referer: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @GET("member/{username}/topics")
    fun getUserTopics(@Path("username") userName: String, @Query("p") page: Int): Single<String>

    //获取用户收藏的节点
    @GET("/")
    fun getMyNodes(): Single<String>

    //获取用户收藏的主题
    @GET()
    fun getMyTopics(): Single<String>

    //获取回复需要的参数
    @GET("t/{topicId}")
    fun getReplyOnce(@Path("topicId") topicId: Int): Single<String>

    //回复
    @Headers("Origin: https://www.v2ex.com", "Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("t/{topicId}")
    fun reply(@Header("Referer") refer:String, @Path("topicId") topicId: Int, @Field("content") content: String, @Field("once") once: String): Single<String>


}

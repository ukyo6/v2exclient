package com.ukyoo.v2client.repository

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.util.async
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

/**
 * 主题详情
 */
class DetailRepository {

    @Inject
    @Named("non_cached")
    lateinit var apiService: HtmlService  //需要解析html的请求
    @Inject
    lateinit var jsonService: JsonService //返回json的请求

    var replyList = ObservableArrayList<ReplyModel>()


    //查看回复
    fun getRepliesByTopicId(topicId: Int, isRefresh: Boolean){
        jsonService.getRepliesByTopicId(topicId)
            .async()
            .map {
                if (isRefresh) {
                    replyList.clear()
                }
                replyList.addAll(it)
                return@map it
            }.doOnSubscribe {

            }.doFinally {

            }.subscribe({

            }, {

            })
    }


    //查看主题信息
    fun getTopicsByTopicId(topicId: Int, isRefresh: Boolean): Single<ArrayList<TopicModel>> {
        return jsonService.getTopicByTopicId(topicId)
            .async()
    }


}


package com.ukyoo.v2client.repository

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.data.entity.TopicWithReplyListModel
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named

/**
 * 主题详情Repository
 */
class DetailRepository {

    @Inject
    @Named("non_cached")
    lateinit var htmlService: HtmlService  //需要解析html的请求
    @Inject
    lateinit var jsonService: JsonService //返回json的请求

    var replyList = ObservableArrayList<ReplyModel>()


    companion object {
        //单例
        @Volatile
        private var instance: DetailRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: DetailRepository().also { instance = it }
            }
    }

    /**
     *  回复列表
     */
    fun getRepliesByTopicId(topicId: Int, isRefresh: Boolean): LiveData<List<ReplyModel>> {
        val list:LiveData<List<ReplyModel>> = MutableLiveData<List<ReplyModel>>()

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

        return list
    }


    /**
     *  查看主题信息
     */
    fun getTopicInfo(topicId: Int) {

        jsonService.getTopicByTopicId(topicId)
            .async()
            .doOnSubscribe {

            }.doFinally {

            }.subscribe({
//                mTopic = it[0]

                getRepliesByTopicId(topicId, true)
            }, {

            })
    }

    /**
     *  查看主题信息和回复
     */
    fun getTopicAndRepliesByTopicId(topicId: Int, isRefresh: Boolean) {
        htmlService.getTopicAndRepliesByTopicId(topicId, getPage(isRefresh))
            .async()
            .map { response ->
                return@map TopicWithReplyListModel().parse(response, true, topicId)
            }
            .doOnSubscribe {

            }.doFinally {

            }.subscribe({
                //更新主题和回复列表
//                loadMore.set(it.currentPage < it.totalPage)
//
//                if (isRefresh) {
//                    empty.set(it.replies.isEmpty())
//                    replyList.clear()
//                    multiDataList.clear()
//                    multiDataList.add(it.topic) //主题内容
//                }
//
//                replyList.addAll(it.replies)
//                multiDataList.addAll(it.replies) //回复列表
            }, {

            })
    }


    private fun getPage(isRefresh: Boolean) = if (isRefresh) 1 else (replyList.size / 100) + 1
}


package com.ukyoo.v2client.data.repository

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModelNew
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named

class TopicsRepository {

    @Inject
    @Named("non_cached")
    lateinit var apiService: HtmlService  //需要解析html的请求
    @Inject
    lateinit var jsonService: JsonService //返回json的请求

    companion object {
        //单例
        @Volatile
        private var instance: TopicsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TopicsRepository().also { instance = it }
            }
    }


    //loadData by tabId
    fun loadDataByTab(isRefresh: Boolean, tab: String): ObservableArrayList<TopicModelNew> {
        val list = ObservableArrayList<TopicModelNew>()
        apiService.queryTopicsByTab(tab)
            .async()
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map TopicListModel().parse(response).apply {
                    list.addAll(this)
                }
            }
//            .doOnSubscribe {
//                startLoad()
//            }.doAfterTerminate {
//                stopLoad()
//                empty.set(list.isEmpty())
//            }
            .subscribe({}, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })

        return list
    }


    //loadData by topicName
    fun loadDataByName(isRefresh: Boolean, name: String): ObservableArrayList<TopicModelNew> {
        val list = ObservableArrayList<TopicModelNew>()
        jsonService.queryTopicsByName(name)
            .async()
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map response.apply {
                    list.addAll(this)
                }
            }
//            .doOnSubscribe {
//                startLoad()
//            }.doAfterTerminate {
//                stopLoad()
//                empty.set(list.isEmpty())
//            }
            .subscribe({}, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })

        return list
    }
}
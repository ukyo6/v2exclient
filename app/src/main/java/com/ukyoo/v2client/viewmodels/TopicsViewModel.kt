package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class TopicsViewModel @Inject constructor(@Named("non_cached") private var apiService: HtmlService, private var jsonService: JsonService) :
    PagedViewModel() {


    internal lateinit var name: String //topicName
    internal lateinit var tab: String  //tabId

    var list = ObservableArrayList<TopicModel>()

    //loadData by tabId
    fun loadDataByTab(isRefresh: Boolean): Single<ArrayList<TopicModel>> {

        return apiService.queryTopicsByTab(tab)
            .async()
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map TopicListModel().parse(response).apply {
                    list.addAll(this)
                }
            }.doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }
    }


    //loadData by topicName
    fun loadDataByName(isRefresh: Boolean): Single<ArrayList<TopicModel>> {

        return jsonService.queryTopicsByName(name)
            .async()
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map response.apply {
                    list.addAll(this)
                }
            }.doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }
    }
}


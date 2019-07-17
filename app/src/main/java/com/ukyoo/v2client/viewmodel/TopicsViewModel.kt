package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.TopicsRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel @Inject constructor(var repository: TopicsRepository) : PagedViewModel() {

    lateinit var name: String //topicName
    lateinit var tab: String  //tabId

    var isRefresh: Boolean = false


    val topics: LiveData<ArrayList<TopicModel>> = MutableLiveData()

    fun haha(){
        val just = Flowable.just(1)
    }


    fun getDataByName() {
        repository.loadDataByName(isRefresh, name)
    }

    fun getDataByTab() {
        repository.loadDataByTab(isRefresh, tab)
    }


}


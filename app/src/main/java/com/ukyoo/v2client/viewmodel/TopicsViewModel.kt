package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.TopicsRepository
import javax.inject.Inject

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel @Inject constructor(var repository: TopicsRepository) : PagedViewModel() {

    private val _name: MutableLiveData<String> = MutableLiveData()         //topicName
    private val _tabId: MutableLiveData<String> = MutableLiveData()   //tabId

    var isRefresh: Boolean = false


    val topics: LiveData<ArrayList<TopicModel>> = MutableLiveData()


    fun setTopicName(topicName: String) {
        _name.value = topicName
    }

    fun setTopicId(tabId: String){
        _tabId.value = tabId
    }



    fun getDataByName() {
        repository.loadDataByName(isRefresh, _name.value)
    }

    fun getDataByTab() {
        repository.loadDataByTab(isRefresh, _tabId.value)
    }
}


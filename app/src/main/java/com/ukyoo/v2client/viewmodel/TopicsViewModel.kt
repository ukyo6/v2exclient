package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.TopicsRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.async
import javax.inject.Inject

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel @Inject constructor(var repository: TopicsRepository) : PagedViewModel() {

    private val param: MutableLiveData<Param> = MutableLiveData()

    fun setTopicName(name: String) {
        param.value = Param("", name)
    }

    fun setNodeId(nodeId: String) {
        param.value = Param(nodeId, "")
    }


    //主题列表
    var topics: LiveData<ArrayList<TopicModel>> = Transformations.switchMap(param) { value ->
        when {
            value.nodeName.isNotEmpty() -> LiveDataReactiveStreams.fromPublisher(
                repository.loadDataByName(true, value.nodeName)
                    .async()
            )
            value.nodeId.isNotEmpty() -> LiveDataReactiveStreams.fromPublisher(
                repository.loadDataByTab(true, value.nodeId)
                    .async()
            )
            else -> AbsentLiveData.create()
        }
    }


    data class Param(var nodeId: String, var nodeName: String)
}


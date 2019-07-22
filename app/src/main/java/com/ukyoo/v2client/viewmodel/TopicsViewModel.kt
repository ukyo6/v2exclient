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
    val topics: LiveData<ArrayList<TopicModel>> = Transformations.switchMap(param) { value ->

        if (value == null || (value.nodeId.isBlank() && value.nodeName.isBlank())) {
            AbsentLiveData.create()
        } else if (value.nodeId.isNotBlank()) {
            LiveDataReactiveStreams.fromPublisher(
                repository.loadDataByTab(true, value.nodeId)
                    .async()
            )
        } else {
            LiveDataReactiveStreams.fromPublisher(
                repository.loadDataByTab(true, value.nodeName)
                    .async()
            )
        }
    }

    /**
     * 重试
     */
    fun retry(){
        val nodeId = param.value?.nodeId
        val nodeName = param.value?.nodeName
        if (nodeId != null && nodeName != null) {
            param.value = Param(nodeId, nodeName)
        }
    }


    data class Param(var nodeId: String, var nodeName: String)
}


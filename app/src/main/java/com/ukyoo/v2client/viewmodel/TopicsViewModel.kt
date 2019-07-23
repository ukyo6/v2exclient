package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.Resource
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
    val topics: LiveData<Resource<ArrayList<TopicModel>>> = Transformations.switchMap(param) { value ->
        value.ifExists { nodeId, nodeName ->
            if (nodeId.isNotBlank()) {
                repository.loadDataByTab(true, value.nodeId)
            } else {
                repository.loadDataByTab(true, value.nodeName)
            }
        }
    }

    /**
     * 重试
     */
    fun retry() {
        val nodeId = param.value?.nodeId
        val nodeName = param.value?.nodeName
        if (nodeId != null && nodeName != null) {
            param.value = Param(nodeId, nodeName)
        }
    }



    data class Param(val nodeId: String, val nodeName: String) {
        fun <T> ifExists(f: (String, String) -> LiveData<T>): LiveData<T> {
            return if (nodeId.isBlank() && nodeName.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(nodeId, nodeName)
            }
        }
    }
}


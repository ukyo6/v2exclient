package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.repository.TopicsRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.async
import javax.inject.Inject

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel @Inject constructor(var repository: TopicsRepository) : AutoDisposeViewModel() {

    private val param: MutableLiveData<Param> = MutableLiveData()

    fun setNodeName(name: String) {
        param.value = Param("", name)
    }

    fun setNodeId(nodeId: String) {
        param.value = Param(nodeId, "")
    }

    //主题列表
    val topics: LiveData<Resources<ArrayList<TopicModel>>> = Transformations.switchMap(param) { value ->
        value.ifExists { nodeId, nodeName ->
            if (nodeId.isNotBlank()) {
                loadTopicData(nodeId)
            } else {
                loadTopicData(nodeName)
            }
        }
    }

    /**
     * 主题列表
     */
    private fun loadTopicData(tabId: String): LiveData<Resources<ArrayList<TopicModel>>> {
        val result = MutableLiveData<Resources<ArrayList<TopicModel>>>()

        repository.loadDataByTab(true, tabId)
            .async()
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this)
            .subscribe(
                { data ->
                    result.value = Resources.success(data)
                },
                { error ->
                    result.value = Resources.error(ErrorHanding.handleError(error))
                }
            )
        return result
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


package com.ukyoo.v2client.ui.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.entity.DetailModel
import com.ukyoo.v2client.repository.DetailRepository
import com.ukyoo.v2client.util.AbsentLiveData
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: DetailRepository
) : AutoDisposeViewModel() {

    var replyContent = ObservableField<String>()  //回复内容

    //topicId
    private val _topicId = MutableLiveData<Int>()

    fun setTopicId(topicId: Int) {
        if (_topicId.value == topicId) {
            return
        }
        _topicId.value = topicId
    }

    /**
     *  主题信息和回复列表
     */
    val topicAndReplies: LiveData<Resources<DetailModel>> = Transformations.switchMap(_topicId) { topicId ->
        if (topicId == null) {
            AbsentLiveData.create()
        } else {
            repository.getTopicInfoAndReplies(topicId, true)
        }
    }


    val replyResult: LiveData<Resources<String>> = MutableLiveData<Resources<String>>()


    class DirectData<Y> {

        val result = MediatorLiveData<Y>()

        private var mSource: LiveData<Y>? = null

        fun addSource(newLiveData: LiveData<Y>) {
            if (mSource === newLiveData) {
                return
            }
            if (mSource != null) {
                result.removeSource(mSource!!)
            }
            mSource = newLiveData
            if (mSource != null) {
                result.addSource(mSource!!, Observer { y -> result.setValue(y) })
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * 重试
     */
    fun refreshDetail() {
        _topicId.value?.let {
            _topicId.value = it
        }
    }
}
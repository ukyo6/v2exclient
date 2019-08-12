package com.ukyoo.v2client.ui.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.orhanobut.logger.Logger
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.entity.DetailModel
import com.ukyoo.v2client.repository.DetailRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import retrofit2.HttpException
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    val repository: DetailRepository
) : AutoDisposeViewModel() {

    var replyContent = ObservableField<String>()  //回复内容

    //根据topicId查询主题信息和回复
    private val _topicId = MutableLiveData<Int>()

    fun setTopicId(topicId: Int) {
        if (_topicId.value == topicId) {
            return
        }
        _topicId.value = topicId
    }

    //主题信息和回复
    val topicAndReplies: LiveData<Resource<DetailModel>> = Transformations.switchMap(_topicId) { topicId ->
        if (topicId == null) {
            AbsentLiveData.create()
        } else {
            repository.getTopicInfoAndRepliesByTopicId(topicId, true)
        }
    }

    /**
     * 重试
     */
    fun retry() {
        _topicId.value?.let {
            _topicId.value = it
        }
    }

    /**
     * 回复
     */
    fun reply() {
        repository.reply(_topicId.value, replyContent.get())
            .async()
            .autoDisposable(this@DetailViewModel)
            .subscribe({ response ->
                ErrorHanding.getProblemFromHtmlResponse(response).apply {
                    ToastUtil.shortShow(this)
                    Logger.d(this)
                }
            }, { throwable ->
                if (throwable is HttpException && throwable.code() == 302) {
                    Logger.d("回复成功了")
                } else {
                    ToastUtil.shortShow(ErrorHanding.handleError(throwable))
                }
            })
    }
}
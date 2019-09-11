package com.ukyoo.v2client.ui.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.entity.DetailModel
import com.ukyoo.v2client.repository.DetailRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import retrofit2.HttpException
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: DetailRepository
) : AutoDisposeViewModel() {

    var replyContent = ObservableField<String>()  //回复内容

    //topicId
    private val _topicId = MutableLiveData<Int>()

    fun setTopicId(topicId: Int?) {
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
            getDetail(topicId, true)
        }
    }

    private fun getDetail(topicId: Int, refresh: Boolean): LiveData<Resources<DetailModel>> {
        val result = MutableLiveData<Resources<DetailModel>>()

        repository.getDetail(topicId, refresh, 1)
            .async()
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this)
            .subscribe({
                result.setValue(Resources.success(it))
            }, {
                if (it is HttpException && it.code() == 302) {  //重定向 需要重新登录
                    ToastUtil.shortShow("查看本主题需要登录")
                } else {
                    val errMsg = ErrorHanding.handleError(it)
                    result.setValue(Resources.error(errMsg))
                }
            })

        return result
    }

    //回复结果
    val replyResult = MutableLiveData<Resources<Unit>>()

    /**
     * 回复
     */
    fun reply(topicId: Int?, replyContent: String) {
        topicId?.let {
            repository.reply(topicId, replyContent)
                .async()
                .doOnSubscribe { replyResult.setValue(Resources.loading()) }
                .autoDisposable(this)
                .subscribe({
                    val errMsg = ErrorHanding.getProblemFromHtmlResponse(it)
                    replyResult.setValue(Resources.error(errMsg))
                }, { throwable ->
                    if (throwable is HttpException && throwable.code() == 302) {  //重定向 回复成功刷新
                        replyResult.setValue(Resources.success(Unit))
                    } else {
                        replyResult.setValue(Resources.error(ErrorHanding.handleError(throwable)))
                    }
                })
        }
    }


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

    /**
     * 重试
     */
    fun refreshDetail() {
        _topicId.value?.let {
            _topicId.value = it
        }
    }
}
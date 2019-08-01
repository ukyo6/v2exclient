package com.ukyoo.v2client.ui.detail

import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.material.math.MathUtils
import com.orhanobut.logger.Logger
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.entity.DetailModel
import com.ukyoo.v2client.repository.DetailRepository
import com.ukyoo.v2client.util.*
import io.reactivex.Flowable
import retrofit2.HttpException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class DetailViewModel @Inject constructor(
    var repository: DetailRepository,
    @Named("non_cached")
    var htmlService: HtmlService
) : AutoDisposeViewModel() {


    var replyContent = ObservableField<String>()  //回复内容

    //topicId
    private val _topicId: MutableLiveData<Int> = MutableLiveData()

    fun setTopicId(topicId: Int) {
        if (_topicId.value == topicId) {
            return
        }
        _topicId.value = topicId
    }

    //主题信息和回复列表
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
     * 获取回复需要的ONCE
     */
    fun getReplyOnce(topicId: Int) {

        htmlService.getReplyOnce(topicId)
            .async()
            .autoDisposable(this)
            .subscribe({ content ->
                val pattern = Pattern.compile("<input type=\"hidden\" value=\"([0-9]+)\" name=\"once\" />")
                val matcher = pattern.matcher(content)
                if (matcher.find()) {
                    val once = matcher.group(1)
                    //回复
                    reply(once, topicId)
                } else {
                    ToastUtil.shortShow("请登录")
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    /**
     * 回复
     */
    private fun reply(once: String, topicId: Int) {
        val replyContents = replyContent.get()
        if (TextUtils.isEmpty(replyContents)) {
            ToastUtil.shortShow("回复内容不得为空")
            return
        }

        val url = "https://www.v2ex.com/t/$topicId"
        replyContents?.let {
            htmlService.reply(url, topicId, replyContents, once)
                .async()
                .autoDisposable(this)
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
}
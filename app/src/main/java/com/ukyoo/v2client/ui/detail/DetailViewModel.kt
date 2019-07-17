package com.ukyoo.v2client.ui.detail

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.repository.DetailRepository
import com.ukyoo.v2client.util.*
import retrofit2.HttpException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class DetailViewModel @Inject constructor(
    private var jsonApi: JsonService,
    @Named("cached") private var htmlService: HtmlService
) : PagedViewModel() {

    var multiDataList = ObservableArrayList<Any>()

    var topic = ObservableField<TopicModel>() //主题

    var replyContent = ObservableField<String>()  //回复内容

    //topicId
    private val _topicId: MutableLiveData<Int> = MutableLiveData()

    fun setTopicId(topicId: Int) {
        if(_topicId.value == topicId) {
            return
        }
        _topicId.value = topicId
    }


    //回复列表
    val replyList: LiveData<List<ReplyModel>> = Transformations.switchMap(_topicId) { topicId ->
        if(topicId == null){
             AbsentLiveData.create()
        } else {
            DetailRepository.getInstance().getTopicInfo(topicId)

            DetailRepository.getInstance().getRepliesByTopicId(topicId, true)
        }
    }

    /**
     * 获取回复需要的ONCE
     */
    fun getReplyOnce(topicId: Int) {
        htmlService.getReplyOnce(topicId)
            .async()
            .bindLifecycle(this)
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
                .bindLifeCycle(this.lifecycleOwner!!)
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
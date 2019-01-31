package com.ukyoo.v2client.ui.detail

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.data.entity.TopicWithReplyListModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.util.apply
import io.reactivex.Single
import retrofit2.HttpException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class DetailViewModel @Inject constructor(
    private var jsonApi: JsonService,
    @Named("non_cached") private var htmlService: HtmlService,
    @Named("cached") private var htmlService2: HtmlService
) : PagedViewModel() {

    var page: Int = 0

    var multiDataList = ObservableArrayList<Any>()
    var replyList = ObservableArrayList<ReplyModel>() //回复
    var topic = ObservableField<TopicModel>() //主题

    var replyContent = ObservableField<String>()  //回复内容

    fun getRepliesByTopicId(topicId: Int, isRefresh: Boolean): Single<ArrayList<ReplyModel>> {
        return jsonApi.getRepliesByTopicId(topicId)
            .async()
            .map {
                if (isRefresh) {
                    replyList.clear()
                }
                replyList.addAll(it)
                return@map it
            }
            .doOnSubscribe {
                startLoad()
            }
            .doAfterTerminate {
                stopLoad()
            }
    }

    fun getTopicsByTopicId(topicId: Int, isRefresh: Boolean): Single<ArrayList<TopicModel>> {
        return jsonApi.getTopicByTopicId(topicId)
            .async()
            .doOnSubscribe {
                startLoad()
            }
            .doAfterTerminate {
                stopLoad()
            }
    }

    /**
     * 获取主题和回复
     */
    fun getTopicAndRepliesByTopicId(topicId: Int, isRefresh: Boolean) {
        htmlService2.getTopicAndRepliesByTopicId(topicId, getPage(isRefresh))
            .async()
            .map { response ->
                return@map TopicWithReplyListModel().parse(response, true, topicId)
            }
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
            }.subscribe({
                //更新主题和回复列表
                loadMore.set(it.currentPage < it.totalPage)

                if (isRefresh) {
                    empty.set(replyList.isEmpty())
                    replyList.clear()
                    multiDataList.clear()
                    multiDataList.add(it.topic) //主题内容
                }

                replyList.addAll(it.replies)
                multiDataList.addAll(it.replies) //回复列表
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    private fun getPage(isRefresh: Boolean) = if (isRefresh) 1 else (replyList.size / 100) + 1

    /**
     * 获取回复需要的ONCE
     */
    fun getReplyOnce(topicId: Int) {
        htmlService2.getReplyOnce(topicId)
            .async()
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
    fun reply(once: String, topicId: Int) {
        val replyContents = replyContent.get()
        if (TextUtils.isEmpty(replyContents)) {
            ToastUtil.shortShow("回复内容不得为空")
            return
        }

        val url = "https://www.v2ex.com/t/$topicId"
        replyContents?.let {
            htmlService2.reply(url, topicId, replyContents, once)
                .async()
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
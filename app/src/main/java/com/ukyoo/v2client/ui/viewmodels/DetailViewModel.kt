package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.entity.TopicWithReplyListModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import javax.inject.Inject

class DetailViewModel @Inject constructor(private var jsonApi: JsonService, private var htmlService: HtmlService) :
    PagedViewModel() {

    var topicId: Int = 0

    var page: Int = 0

    var replyList = ObservableArrayList<ReplyModel>()
    var multiDataList = ObservableArrayList<Any>()

    var topic = ObservableField<TopicModel>()

    fun getRepliesByTopicId(isRefresh: Boolean): Single<ArrayList<ReplyModel>> {
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

    fun getTopicsByTopicId(isRefresh: Boolean): Single<ArrayList<TopicModel>> {
        return jsonApi.getTopicByTopicId(topicId)
            .async()
            .doOnSubscribe {
                startLoad()
            }
            .doAfterTerminate {
                stopLoad()
            }
    }

    fun getTopicAndRepliesByTopicId(isRefresh: Boolean) {
        htmlService.getTopicAndRepliesByTopicId(topicId, page)
            .async()
            .map { response ->
                return@map TopicWithReplyListModel().parse(response, page == 1, topicId)
            }
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
            }.subscribe({
                //更新回复列表
                if (isRefresh) {
                    replyList.clear()
                    multiDataList.clear()
                }

                replyList.addAll(it.replies)
                multiDataList.apply {
                    add(it.topic) //主题内容
                    addAll(replyList) //回复列表
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

}
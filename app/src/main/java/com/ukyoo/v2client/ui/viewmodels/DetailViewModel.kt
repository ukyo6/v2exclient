package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.*
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import javax.inject.Inject

class DetailViewModel @Inject constructor(var jsonApi: JsonService, var htmlService: HtmlService) : PagedViewModel() {

    var topicId: Int = 0

    var page: Int = 0

    var replyList = ObservableArrayList<ReplyModel>()

    fun getRepliesByTopicId(isRefresh: Boolean): Single<ArrayList<ReplyModel>> {
        return jsonApi.getRepliesByTopicId(topicId)
            .async()
            .map {
                if (isRefresh) replyList.clear()

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

    fun getTopicAndRepliesByTopicId(isRefresh: Boolean): Single<TopicWithReplyListModel> {
        return htmlService.getTopicAndRepliesByTopicId(topicId, page)
            .async()
            .map { response ->
                if(isRefresh)replyList.clear()

                return@map TopicWithReplyListModel().parse(response, page == 1, topicId)
                    .apply {
                        replyList.addAll(this.replies)
                    }
            }
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
            }
    }

}
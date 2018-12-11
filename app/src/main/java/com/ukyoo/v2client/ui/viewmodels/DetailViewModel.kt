package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import javax.inject.Inject

class DetailViewModel @Inject constructor(var jsonApi: JsonService) : PagedViewModel() {

    lateinit var topicId: String

    var replyList = ObservableArrayList<ReplyModel>()

    fun getRepliesByTopicId(isRefresh: Boolean): Single<ArrayList<ReplyModel>> {
        return jsonApi.getRepliesByTopicId(topicId)
            .async()
            .doOnSubscribe {
                startLoad()
            }
            .doAfterTerminate {
                stopLoad()
            }
    }

    fun getTopicAndRepliesByTopicId(isRefresh: Boolean): Any? {
        return null
    }
}
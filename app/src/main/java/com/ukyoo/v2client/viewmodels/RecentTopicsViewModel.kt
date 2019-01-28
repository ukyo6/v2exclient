package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.entity.TopicListModel
import com.ukyoo.v2client.data.entity.TopicModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named

class RecentTopicsViewModel @Inject constructor(
    @Named("non_cached") private var htmlService: HtmlService
) : BaseViewModel() {

    var createdTopics = ObservableArrayList<TopicModel>()  //创建的主题

    /**
     * 获取创建的主题
     */
    fun getUserTopics(username: String) {
        htmlService.getUserTopics(username, 1)
            .async()
            .subscribe({ response ->
                val topics = TopicListModel().parse(response)
                createdTopics.apply {
                    clear()

                    for (topic in topics) {
                        topic.member.avatar = "1"
                    }
                    addAll(topics)
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }
}
package com.ukyoo.v2client.viewmodel

import android.text.method.TextKeyListener.clear
import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import java.util.Collections.addAll
import javax.inject.Inject
import javax.inject.Named

class RecentTopicsViewModel @Inject constructor(
    @Named("non_cached") private var htmlService: HtmlService
) : BaseViewModel() {

//    var createdTopics = ObservableArrayList<TopicModelNew>()  //创建的主题

    /**
     * 获取创建的主题
     */
//    fun getUserTopics(username: String) {
//        htmlService.getUserTopics(username, 1)
//            .async()
//            .subscribe({ response ->
//                val topics = TopicListModel().parse(response)
//                createdTopics.apply {
//                    clear()
//
//                    for (topic in topics) {
//                        topic.member.avatar = "1"
//                    }
//                    addAll(topics)
//                }
//            }, {
//                ToastUtil.shortShow(ErrorHanding.handleError(it))
//            })
//    }
}
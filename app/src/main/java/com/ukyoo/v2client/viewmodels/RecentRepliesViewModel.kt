package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.entity.MemberModel1
import com.ukyoo.v2client.entity.ReplyListModel
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named

class RecentRepliesViewModel @Inject constructor(
    @Named("non_cached") private var htmlService: HtmlService
) : BaseViewModel() {

    var createdReplies = ObservableArrayList<ReplyListModel.ReplyItemModel>()

    /**
     * 获取用户回复
     */
    fun getUserReplies(username: String) {
        htmlService.getUserReplies(username, 1)
            .async()
            .subscribe({ response ->
                createdReplies.apply {

                    addAll(ReplyListModel().parse(response))
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }
}
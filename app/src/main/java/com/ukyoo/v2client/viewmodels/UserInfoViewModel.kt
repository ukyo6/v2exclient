package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.MemberModel1
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.TimeUtils
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named


class UserInfoViewModel @Inject constructor(
    private var jsonService: JsonService,
    @Named("non_cached") private var htmlService: HtmlService
) : PagedViewModel() {

    var memberModel = ObservableField<MemberModel1>()  //用户信息

    var registerTimeAndNum = ObservableField<String>()  //注册时间

    fun getUserInfo(username: String, isRefresh: Boolean) {
        jsonService.getUserInfo(username)
            .async()
            .map {
                return@map it.apply {
                    val created = TimeUtils.milliseconds2String(this.created * 1000.toLong())
                    registerTimeAndNum.set("V2EX 第 " + this.id + " 号会员，加入于" + created)
                    transfer()
                }
            }
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
            }.subscribe({
                memberModel.set(it)

                getUserTopics(it)
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    var createdTopics = ObservableArrayList<TopicModel>()  //创建的主题

    /**
     * 获取创建的主题
     */
    private fun getUserTopics(memberModel: MemberModel1) {
        htmlService.getUserTopics(memberModel.username, 1)
            .async()
            .subscribe({ response ->
                val topics = TopicListModel().parse(response)
                createdTopics.apply {
                    clear()

                    for (topic in topics) {
                        topic.member.avatar = memberModel.avatar_large
                    }
                    addAll(topics)
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    /**
     * 获取用户回复
     */
    fun getUserReplies() {

    }


}
package com.ukyoo.v2client.repository

import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.entity.ReplyItem
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * 用户信息(详细)
 * 发表的主题列表
 * 回复列表
 */
@Singleton
class UserInfoRepository @Inject constructor(
    @Named("non_cached")
    var htmlService: HtmlService, var jsonService: JsonService
) {



    /**
     * 获取用户详细信息
     */
    fun getUserInfo(username: String): Flowable<MemberModel> {
        return jsonService.getUserInfo(username)
            .async()
            .doOnSubscribe {

            }.doFinally {

            }
    }

    /**
     * 获取创建的主题
     */
    private fun getUserTopics(memberModel: ReplyItem.MemberInfo) {
        memberModel.username.let {
            htmlService.getUserTopics(it, 1)
                .async()
                .subscribe({ response ->
                    //                    val topics = TopicListModel().parse(response)
//                    createdTopics.apply {
//                        clear()
//
//                        for (topic in topics) {
//                            topic.member?.avatar = memberModel.avatar_large.toString()
//                        }
////                        addAll(topics)
//                    }
                }, {
                    ToastUtil.shortShow(ErrorHanding.handleError(it))
                })
        }
    }

    /**
     * 获取用户回复
     */
    fun getUserReplies() {

    }
}
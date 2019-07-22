package com.ukyoo.v2client.entity

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ukyoo.v2client.ui.detail.DetailActivity

/**
 * 详情页数据类
 */
data class DetailModel(

    var topicInfo: TopicInfo? = null,

    var replies: List<ReplyItem>? = null,

    var currentPage: Int = 0,

    var totalPage: Int = 0
)

/**
 * 主题信息
 */
data class TopicInfo(
    var id: Int = 0,
    var title: String = "",
    var member: UserInfo? = null,
    var node: NodeInfo? = null,
    var url: String = "",
    var content: String = "",
    var contentRendered: String = "",
    var replies: Int = 0, //评论数量
    var created: String = "",
    var lastModified: Long = 0,
    var lastTouched: Long = 0
) : MultiItemEntity {
    override fun getItemType(): Int {
        return DetailActivity.ITEM_TOPIC
    }

    /**
     * 用户信息
     */
    data class UserInfo(
        var avatar: String = "",
        var username: String = ""
    )

    /**
     * 节点信息
     */
    data class NodeInfo(
        var name: String = "",
        var title: String = ""
    )
}

/**
 * 回复条目
 */
data class ReplyItem(
    var id: Int = 0,
    var thanks: Int = 0,
    var content: String = "",
    var contentRendered: String = "",
    var member: MemberInfo? = null,
    var created: String = "",
    var lastModified: Long = 0
) : MultiItemEntity {
    override fun getItemType(): Int {
        return DetailActivity.ITEM_REPLY
    }

    //用户信息
    data class MemberInfo(
        var id: Int = 0,
        var username: String = "",
        var tagline: String = "",
        var avatar: String = "",      //73*73
        var website: String = "",
        var github: String = "",
        var twitter: String = "",
        var location: String = ""
    )
}

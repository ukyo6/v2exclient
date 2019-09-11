package com.ukyoo.v2client.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * 主题列表数据类
 */
@Parcelize
data class TopicModel(
    var member: UserInfo? = null,
    var node: NodeInfo? = null,

    var id: Int = 0,         //帖子id
    var title: String = "",  //标题
    var replies: Int = 0,    //回复数量
    var created: String = "", //创建时间
    var contentRendered: String = ""
) : Parcelable {
    /**
     * 用户信息
     */
    @Parcelize
    data class UserInfo(
        var avatar: String = "",
        var username: String = ""
    ) : Parcelable

    /**
     * 节点信息
     */
    @Parcelize
    data class NodeInfo(
        var name: String = "",
        var title: String = ""
    ) : Parcelable
}






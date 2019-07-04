package com.ukyoo.v2client.entity

import com.ukyoo.v2client.data.entity.NodeModel


class TopicModelNew {

    lateinit var member: MemberModelNew
    lateinit var node: NodeModel

    var id: Int = 0
    var title: String = ""
    var replies: Int = 0
    var created: String = ""
}
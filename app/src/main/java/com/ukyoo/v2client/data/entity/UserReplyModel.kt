package com.ukyoo.v2client.data.entity

data class UserReplyModel(

    var replyTo: String = "",
    var replyTime: String = "",
    var topicTitle: String = "",
    var topicId: Int = 0,

    var nodeName: String = "",
    var nodeId: String = "",
    var replies: Int = 0,
    var replyContent: String = ""
)
package com.ukyoo.v2cliecnt.entity

data class TopicEntity(
    val content: String,
    val content_rendered: String,
    val created: Int,
    val id: Int,
    val last_modified: Int,
    val last_reply_by: String,
    val last_touched: Int,
    val replies: Int,
    val title: String,
    val url: String,
    var nodeEntity: NodeEntity,
    var memberEntity: MemberEntity
)
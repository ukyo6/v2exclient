package com.ukyoo.v2client.entity

data class NodeEntity(
    val avatar_large: String,
    val avatar_mini: String,
    val avatar_normal: String,
    val footer: String,
    val header: String,
    val id: Int,
    val name: String,
    val parent_node_name: String,
    val root: Boolean,
    val stars: Int,
    val title: String,
    val title_alternative: String,
    val topics: Int,
    val url: String
)
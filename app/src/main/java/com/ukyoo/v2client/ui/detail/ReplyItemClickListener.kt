package com.ukyoo.v2client.ui.detail

import com.ukyoo.v2client.data.entity.ReplyModel

interface ReplyItemClickListener{
    fun clickUserAvatar()

    fun clickItemToReply(item: ReplyModel)
}
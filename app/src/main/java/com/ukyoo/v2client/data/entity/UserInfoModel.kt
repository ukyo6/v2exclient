package com.ukyoo.v2client.data.entity

import com.ukyoo.v2client.util.TimeUtils

/**
 * 用户个人信息(详细)
 */
data class UserInfoModel(
    var avatar_large: String,
    val avatar_mini: String,
    val avatar_normal: String,
    val bio: String,
    val btc: String,
    val created: Int,
    val github: String,
    val id: Int,
    val location: String,
    val psn: String,
    val status: String,
    val tagline: String,
    val twitter: Any,
    val url: String,
    val username: String,
    val website: String
) {

    fun getAvatar() : String{
        return "https:$avatar_large"  //头像链接
    }

    fun getRegisterTimeAndNum() :String{
        return TimeUtils.milliseconds2String(this.created * 1000.toLong()) //注册时间和会员号码
    }

}
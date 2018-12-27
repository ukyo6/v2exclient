package com.ukyoo.v2client.util

import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaderFactory
import com.bumptech.glide.load.model.LazyHeaders
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R

/**
 * 加载图片
 * created by hewei
 */

object ImageUtil {

    fun load(uri: String?, imageView: ImageView, isAvatar: Boolean = false) {
        GlideApp.with(App.instance()).load(uri)
            .into(imageView)
    }

    /**
     * 加载验证码
     */
    fun loadVerifyCode(uri: String?, imageView: ImageView) {
        if (uri == null) return

        val builder = LazyHeaders.Builder()
        val stringSet = SPUtils.getStringSet("cookie")
        stringSet?.forEach {
            builder.addHeader("cookie",it)
        }

        val glideUrl = GlideUrl(
            uri, builder.build()
        )
        GlideApp.with(App.instance()).load(glideUrl).into(imageView)
    }
}
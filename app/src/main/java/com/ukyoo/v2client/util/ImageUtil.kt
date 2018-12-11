package com.ukyoo.v2client.util

import android.graphics.Color
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R

/**
 * 页面描述：ImageUtil
 *
 * Created by ditclear on 2017/10/21.
 */

object ImageUtil {

    fun load(uri: String?, imageView: ImageView, isAvatar: Boolean = false) {
        if (!isAvatar) {
            GlideApp.with(App.instance()).load(uri)
                    .into(imageView)
        } else {
            GlideApp.with(App.instance()).load(uri)
                    .into(imageView)
        }
    }

}
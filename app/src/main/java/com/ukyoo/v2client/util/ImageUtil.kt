package com.ukyoo.v2client.util

import android.graphics.Color
import android.widget.ImageView
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
                imageView.setBackgroundColor(Color.parseColor("#ff00000"))
            imageView.setImageResource(R.drawable.ic_nav_home)

//            Glide.with(App.instance()).load(uri)
//                    .placeholder(R.color.tools_color)
//                    .error(R.color.tools_color)
//                    .into(imageView)
        } else {

            imageView.setBackgroundColor(Color.parseColor("#00ff00"))
            imageView.setImageResource(R.drawable.ic_nav_home)
//            Glide.with(App.instance()).load(uri)
//                    .bitmapTransform(CropCircleTransformation(imageView.context))
//                    .placeholder(R.drawable.ic_face)
//                    .error(R.drawable.ic_face)
//                    .into(imageView)
        }
    }

}
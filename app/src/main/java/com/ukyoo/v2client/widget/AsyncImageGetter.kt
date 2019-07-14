package com.ukyoo.v2client.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.ukyoo.v2client.util.GlideApp


class AsyncImageGetter (var context:Context,var tv:TextView) : Html.ImageGetter {
    override fun getDrawable(source: String?): Drawable? {
        val drawableWrapper = MyDrawableWrapper()

        GlideApp.with(context)
            .asBitmap()
            .load(source)
            .into(BitmapTarget(drawableWrapper))

        return drawableWrapper
    }

    private inner class BitmapTarget(private val drawableWrapper: MyDrawableWrapper) : SimpleTarget<Bitmap>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
        ) {
            //获取原图大小
            val mMaxWidth = tv.width
            val width: Int
            val height: Int
            if (resource.width > mMaxWidth) {
                width = mMaxWidth
                height = mMaxWidth * resource.height / resource.width
            } else {
                width = resource.width
                height = resource.height
            }
            val drawable = BitmapDrawable(context.resources, resource)
            drawable.setBounds(0, 0, width, height)
            drawableWrapper.setBounds(0,0,width,height)
            drawableWrapper.drawable = drawable
            tv.text = tv.text
            tv.invalidate()
        }
    }


    internal inner class MyDrawableWrapper : BitmapDrawable() {
        var drawable: Drawable? = null
        override fun draw(canvas: Canvas) {
            if (drawable != null)
                drawable!!.draw(canvas)
        }
    }
}
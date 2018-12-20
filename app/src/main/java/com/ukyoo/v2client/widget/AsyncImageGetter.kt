package com.ukyoo.v2client.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.ukyoo.v2client.R
import com.ukyoo.v2client.util.GlideApp


class AsyncImageGetter (var context:Context,var tv:TextView) : Html.ImageGetter {


    override fun getDrawable(source: String?): Drawable {
        val drawableWrapper = MyDrawableWrapper()
        val mDrawable: Drawable = context.resources.getDrawable(R.mipmap.ic_launcher)
        mDrawable.setBounds(
            0,
            0,
            mDrawable.intrinsicWidth,
            mDrawable.intrinsicHeight
        )
        drawableWrapper.drawable = mDrawable

        GlideApp.with(context)
            .asBitmap()
            .load(source)
            .into(BitmapTarget(drawableWrapper))

        return mDrawable
    }

    private inner class BitmapTarget(private val myDrawable: MyDrawableWrapper) : SimpleTarget<Bitmap>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
        ) {
            val drawable = BitmapDrawable(context.resources, resource)
            //获取原图大小
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            //自定义drawable的高宽, 缩放图片大小最好用matrix变化，可以保证图片不失真
            drawable.setBounds(0, 0, 500, 500)
            myDrawable.setBounds(0, 0, 500, 500)
            myDrawable.drawable = drawable
            tv.setText(tv.getText())
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
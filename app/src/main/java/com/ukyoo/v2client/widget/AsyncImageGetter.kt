package com.ukyoo.v2client.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ukyoo.v2client.util.GlideApp
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.transition.Transition
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget




class AsyncImageGetter: Html.ImageGetter{
    lateinit var mContext: Context
    lateinit var tv: TextView


    override fun getDrawable(source: String?): Drawable {
        val drawableWrapper = MyDrawableWrapper()


        GlideApp.with(mContext)
            .asBitmap()
            .load(source)
            .into(object : CustomViewTarget<tv,>)



    }

    internal inner class BitmapTarget(private val myDrawable: MyDrawableWrapper) : SimpleTarget<Bitmap>() {
        fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>) {
            val drawable = BitmapDrawable(getResources(), resource)
            //获取原图大小
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            //自定义drawable的高宽, 缩放图片大小最好用matrix变化，可以保证图片不失真
            drawable.setBounds(0, 0, 500, 500)
            myDrawable.setBounds(0, 0, 500, 500)
            myDrawable.setDrawable(drawable)
            tv.setText(tv.getText())
            tv.invalidate()
        }
    }


    internal inner class MyDrawableWrapper : BitmapDrawable() {
        var drawable: Drawable? = null
        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }
    }
}
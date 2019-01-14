package com.ukyoo.v2client.util

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.ukyoo.v2client.App

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
            builder.addHeader("cookie", it)
        }

        val glideUrl = GlideUrl(
            uri, builder.build()
        )
        GlideApp.with(App.instance()).load(glideUrl).into(imageView)
    }


    /**
     * 从图片取色 生成模糊背景
     */
    fun loadBlurBg(imageView: ImageView, bitmap: Bitmap) {
        Palette.Builder(bitmap).generate(Palette.PaletteAsyncListener { palette ->
            //记得判空
            if (palette == null) return@PaletteAsyncListener;
            //palette取色不一定取得到某些特定的颜色，这里通过取多种颜色来避免取不到颜色的情况
            when {
                palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> createLinearGradientBitmap(
                    palette.getDarkVibrantColor(Color.TRANSPARENT),
                    palette.getVibrantColor(Color.TRANSPARENT), imageView
                )
                palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> createLinearGradientBitmap(
                    palette.getDarkMutedColor(Color.TRANSPARENT),
                    palette.getMutedColor(Color.TRANSPARENT), imageView
                )
                else -> createLinearGradientBitmap(
                    palette.getLightMutedColor(Color.TRANSPARENT),
                    palette.getLightVibrantColor(Color.TRANSPARENT), imageView
                )
            }
        })
    }

    private var bgBitmap: Bitmap? = null
    private var mCanvas: Canvas = Canvas()
    private var mPaint: Paint? = Paint()

    //创建线性渐变背景色
    private fun createLinearGradientBitmap(darkColor: Int, color: Int, ivBg: ImageView) {
        val bgColors = IntArray(2)
        bgColors[0] = darkColor
        bgColors[1] = color

        if (bgBitmap == null) {
            bgBitmap = Bitmap.createBitmap(ivBg.width, ivBg.height, Bitmap.Config.ARGB_4444);
        }

        mCanvas.setBitmap(bgBitmap)
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        mPaint?.shader = LinearGradient(
            0f,
            0f,
            0f,
            bgBitmap?.height?.toFloat() ?: 0f,
            bgColors[0],
            bgColors[1],
            Shader.TileMode.CLAMP
        )


        val rectF = RectF(0f, 0f, bgBitmap!!.width.toFloat(), bgBitmap!!.height.toFloat())
        // mCanvas.drawRoundRect(rectF,16,16,mPaint); 这个用来绘制圆角的哈
        mCanvas.drawRect(rectF, mPaint)
        ivBg.setImageBitmap(bgBitmap)
    }
}
package com.ukyoo.v2client.util.binds

import android.graphics.Bitmap
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.Presenter
import com.ukyoo.v2client.util.GlideApp
import com.ukyoo.v2client.util.ImageUtil
import com.ukyoo.v2client.util.ScrimUtil
import com.ukyoo.v2client.widget.RichTextView

/**
 * 页面描述：normal bind class
 *
 * Created by hewei
 */

@BindingAdapter(value = ["url"], requireAll = false)
fun bindUrl(imageView: ImageView, url: String?) {
    ImageUtil.load(url, imageView)
}


//加载验证码
@BindingAdapter(value = ["verifyUrl"])
fun bindVerifyUrl(imageView: ImageView, url: String?) {
    ImageUtil.loadVerifyCode(url, imageView)
}

@BindingAdapter(value = ["richTexts"])
fun bindRichText(textView: RichTextView, text: String?) {
    textView.setRichText(text ?: "")
}


@BindingAdapter(value = ["loadMore", "loadMorePresenter"])
fun bindLoadMore(v: RecyclerView, vm: ViewModel?, presenter: Presenter) {
    v.layoutManager = LinearLayoutManager(v.context)
    v.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager is LinearLayoutManager) {
                //表示是否能向上滚动，false表示已经滚动到底部
                //防止多次拉取同样的数据
                if (!recyclerView.canScrollVertically(1)) {

                }
            }
        }
    })
}

@BindingAdapter(value = ["onRefresh"])
fun bindOnRefresh(v: SwipeRefreshLayout, presenter: Presenter) {
    v.setOnRefreshListener { presenter.loadData(true, null) }
}



//渐变式阴影
@BindingAdapter(value = ["shadow_color", "num_step", "gravity"], requireAll = false)
fun setShadow(view: View, mColor: Int, mNumSteps: Int, mGravity: Int) {
    var color = mColor
    var numSteps = mNumSteps
    var gravity = mGravity
    if (color == 0) {
        color = ContextCompat.getColor(view.context, R.color.shadow)
    }
    if (numSteps == 0) {
        numSteps = 6
    }
    if (gravity == 0) {
        gravity = Gravity.TOP
    }
    view.background = ScrimUtil.makeCubicGradientScrimDrawable(
        color, numSteps,
        gravity
    )
}

//模糊背景图
@BindingAdapter(value = ["blurBg"])
fun bindBlurBg(imageView: ImageView, url: String?) {
    GlideApp.with(App.instance()).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            ImageUtil.loadBlurBg(imageView, resource)
        }
    })
}

//可见性
@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}
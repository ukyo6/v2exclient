package com.ukyoo.v2client.util.binds

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.Presenter
import com.ukyoo.v2client.util.ImageUtil
import com.ukyoo.v2client.util.ScrimUtil
import com.ukyoo.v2client.viewmodel.PagedViewModel
import com.ukyoo.v2client.widget.RichTextView

/**
 * 页面描述：normal bind class
 *
 * Created by hewei
 */

@BindingAdapter(value = ["url", "avatar"], requireAll = false)
fun bindUrl(imageView: ImageView, url: String?, isAvatar: Boolean?) {
    ImageUtil.load(url, imageView, isAvatar = isAvatar ?: false)
}


@BindingAdapter(value = ["verifyUrl"])
fun bindVerifyUrl(imageView: ImageView, url: String?) {
    ImageUtil.loadVerifyCode(url, imageView)
}

@BindingAdapter(value = ["richTexts"])
fun bindRichText(textView: RichTextView, text: String?) {
    textView.setRichText(text ?: "")
}

//@BindingAdapter(value = ["start_color", "icon"], requireAll = false)
//fun bindTransitionArgs(v: View, color: Int, icon: Int?) {
//    v.setTag(R.integer.start_color, color)
//    if (v is FloatingActionButton) {
//        icon?.let { v.setTag(R.integer.fab_icon, icon) }
//    }
//}
//
//@BindingAdapter(value = ["markdown"])
//fun bindMarkDown(v: MarkdownView, markdown: String?) {
//    markdown?.let {
//        v.setMarkdown(markdown)
//    }
//}

@BindingAdapter(value = ["visible"])
fun bindVisibility(v: View, visible: Boolean) {
    v.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["loadMore", "loadMorePresenter"])
fun bindLoadMore(v: RecyclerView, vm: PagedViewModel?, presenter: Presenter) {
    v.layoutManager = LinearLayoutManager(v.context)
    v.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager is LinearLayoutManager) {
                //表示是否能向上滚动，false表示已经滚动到底部
                //防止多次拉取同样的数据
                if (!recyclerView.canScrollVertically(1)) {
                    vm?.let {
                        if (vm.loadMore.get() && !vm.loading.get()) {
                            presenter.loadData(false)
                        }
                    }
                }
            }
        }
    })
}

@BindingAdapter(value = ["onRefresh"])
fun bindOnRefresh(v: SwipeRefreshLayout, presenter: Presenter) {
    v.setOnRefreshListener { presenter.loadData(true) }
}

@BindingAdapter(value = ["vertical"], requireAll = false)
fun bindSlider(v: RecyclerView, vertical: Boolean = true) {

    if (vertical) {
        v.layoutManager = LinearLayoutManager(v.context, RecyclerView.VERTICAL, false)
    } else {
        if (v.onFlingListener == null) {
            PagerSnapHelper().attachToRecyclerView(v)
        }
        v.layoutManager = LinearLayoutManager(v.context, RecyclerView.HORIZONTAL, false)
    }
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

@BindingAdapter(value = ["richText"])
fun setRichText(richTextView: RichTextView, content: String) {
    richTextView.setRichText(content)
}
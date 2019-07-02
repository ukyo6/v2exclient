package com.ukyoo.v2client.inter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.ukyoo.v2client.util.adapter.BindingViewHolder

/**
 * created by hewei
 */
interface ItemClickPresenter<in Any> {
    fun onItemClick(v: View? = null, item: Any)
}

interface ItemDecorator {
    fun decorator(holder: BindingViewHolder<ViewDataBinding>?, position: Int, viewType: Int)
}

interface ItemAnimator {

    fun scrollUpAnim(v: View)

    fun scrollDownAnim(v: View)
}
package com.ukyoo.v2cliecnt.adapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}
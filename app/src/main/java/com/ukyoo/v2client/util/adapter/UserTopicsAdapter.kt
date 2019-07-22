package com.ukyoo.v2client.util.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ukyoo.v2client.data.entity.ReplyListModel
import com.ukyoo.v2client.entity.TopicModel


/**
 *  主题列表页Adapter
 */
class UserTopicsAdapter(layoutResId: Int) :
    BaseQuickAdapter<TopicModel, UserTopicsAdapter.VHolder>(layoutResId) {

    init {
//        var itemPresenter: ItemClickPresenter = null
    }


    override fun convert(helper: VHolder, item: TopicModel?) {
        val binding = helper.binding
        binding.setVariable(BR.item, item)
//        binding.setVariable(BR.presenter, mPresenter)
        binding.executePendingBindings()
    }

    override fun createBaseViewHolder(view: View?): VHolder {
        return VHolder(view)
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(com.ukyoo.v2client.R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class VHolder(view: View?) : BaseViewHolder(view) {

        val binding: ViewDataBinding
            get() = itemView.getTag(com.ukyoo.v2client.R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}




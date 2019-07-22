package com.ukyoo.v2client.util.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ukyoo.v2client.R
import com.ukyoo.v2client.ui.detail.DetailActivity

/**
 * 详情页Adapter 头部和评论列表
 */
class DetailAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    init {
        addItemType(DetailActivity.ITEM_TOPIC, R.layout.item_topic_header)
        addItemType(DetailActivity.ITEM_REPLY, R.layout.item_reply)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        val binding = (helper as TopicListAdapter.VHolder).binding
        binding.setVariable(BR.item, item)
//        binding.setVariable(BR.presenter, mPresenter)
        binding.executePendingBindings()
    }

    override fun createBaseViewHolder(view: View?): TopicListAdapter.VHolder {
        return TopicListAdapter.VHolder(view)
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class VHolder(view: View?) : BaseViewHolder(view) {
        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}



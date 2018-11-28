package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ToTopOrRefreshContract
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle

class TopicsFragment : BaseFragment<FragmentTopicBinding>(),ToTopOrRefreshContract {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<TopicsViewModel>()
    }

    //fragment instance
    companion object {
        private const val TOPIC_TYPE: String = "TOPIC_TYPE"

        fun newInstance(topicType: String): TopicsFragment {
            val fragment = TopicsFragment()
            val bundle = Bundle()
            bundle.putString(TOPIC_TYPE, topicType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        getComponent().inject(this)

        mBinding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        mBinding.recyclerView.adapter = object : BaseQuickAdapter<TopicModel, BaseViewHolder>(R.layout.item_topic) {
            override fun convert(helper: BaseViewHolder?, item: TopicModel?) {
            }
        }

        mBinding.recyclerView.adapter = SingleTypeAdapter(mContext,R.layout.item_topic,viewModel.list)
    }

    /**
     * get data from remote
     */
    override fun loadData(isRefresh: Boolean) {
        val topicType = arguments?.getString(TOPIC_TYPE)
        viewModel.topicId = topicType?:"11111"

        viewModel.loadData(isRefresh = true)
            .bindLifeCycle(this)
            .subscribe({


            },{
                toastFailure(it)
            })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

    /**
     * scrollToTop or refresh
     */
    override fun toTopOrRefresh(){
        if (mBinding.recyclerView.layoutManager is LinearLayoutManager){
            val layoutManager = mBinding.recyclerView.layoutManager as LinearLayoutManager
            if (layoutManager.findLastVisibleItemPosition()> 5){
                mBinding.recyclerView.smoothScrollToPosition(0)
            }else{
                mBinding.recyclerView.smoothScrollToPosition(0)
                loadData(true)
            }
        }
    }
}

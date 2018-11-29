package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.databinding.FragmentTopicBindingImpl
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.inter.ToTopOrRefreshContract
import com.ukyoo.v2client.ui.detail.DetailActivity
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle

class TopicsFragment : BaseFragment<FragmentTopicBinding>(), ToTopOrRefreshContract, ItemClickPresenter<TopicModel> {

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
        val recyclerView = mBinding.recyclerView

        //set adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = SingleTypeAdapter(mContext, R.layout.item_topic, viewModel.list).apply {
            itemPresenter = this@TopicsFragment
        }
    }

    /**
     * get data from remote
     */
    override fun loadData(isRefresh: Boolean) {
        val topicType = arguments?.getString(TOPIC_TYPE)
        viewModel.topicId = topicType ?: "11111"

        viewModel.loadData(isRefresh = true)
            .bindLifeCycle(this)
            .subscribe({

            }, {
                toastFailure(it)
            })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

    override fun lazyLoad() {
        if (!isPrepared || !visible || hasLoadOnce) {
            return
        }
        hasLoadOnce = true
        loadData(true)
    }

    /**
     * scrollToTop or refresh
     */
    override fun toTopOrRefresh() {
        if (mBinding.recyclerView.layoutManager is LinearLayoutManager) {
            val layoutManager = mBinding.recyclerView.layoutManager as LinearLayoutManager
            if (layoutManager.findLastVisibleItemPosition() > 5) {
                mBinding.recyclerView.smoothScrollToPosition(0)
            } else {
                mBinding.recyclerView.smoothScrollToPosition(0)
                loadData(true)
            }
        }
    }

    /**
     * ItemClick
     */
    override fun onItemClick(v: View?, item: TopicModel) {
        val intent = Intent(mContext, DetailActivity::class.java)
        startActivity(intent)
    }
}

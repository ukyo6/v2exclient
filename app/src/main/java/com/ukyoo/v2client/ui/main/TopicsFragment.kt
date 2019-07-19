package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.inter.ToTopOrRefreshContract
import com.ukyoo.v2client.ui.detail.DetailActivity
import com.ukyoo.v2client.util.adapter.TopicListAdapter
import com.ukyoo.v2client.viewmodel.TopicsViewModel

/**
 * 单个主题列表页  (技术/创意/好玩...)
 */
class TopicsFragment : BaseFragment<FragmentTopicBinding>(),
    ToTopOrRefreshContract,
    ItemClickPresenter<TopicModel> {


    lateinit var topicsAdapter:TopicListAdapter

    override fun isLazyLoad(): Boolean = "lazyOpen" == arguments?.get(SOURCE)

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<TopicsViewModel>()
    }

    //fragment instance
    companion object {
        const val NODE_ID: String = "NODE_ID"    //节点列表通过topicId
        const val NODE_NAME: String = "NODE_NAME"    //节点列表通过topicName
        const val TAB_ID: String = "TAB_ID"        //首页ViewPager通过tab
        private const val SOURCE: String = "SOURCE"

        fun newInstance(bundle: Bundle, source: String): TopicsFragment {
            val fragment = TopicsFragment()
            bundle.putString(SOURCE, source)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel

        topicsAdapter = TopicListAdapter(R.layout.item_topic)
        mBinding.recyclerView.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = topicsAdapter
        }

        topicsAdapter.setOnItemClickListener { adapter, view, position ->

            val item: TopicModel = topicsAdapter.data[position]
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra("topic_id", item.id)  //topicId
            startActivity(intent)
        }
    }

    /**
     * get data
     */
    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        val nodeName = arguments?.getString(NODE_NAME)
        val tab = arguments?.getString(TAB_ID)

        if (nodeName != null) {
            viewModel.setTopicName(nodeName)
        } else if (tab != null) {
            viewModel.setNodeId(tab)
        }

        viewModel.topics.observe(this@TopicsFragment, Observer { list ->
            topicsAdapter.setNewData(list)
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
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
                loadData(true, null)
            }
        }
    }

    /**
     * 点击条目跳转详情页
     */
    override fun onItemClick(v: View?, item: TopicModel) {
        val intent = Intent(mContext, DetailActivity::class.java)
        intent.putExtra("topicId", item.id)  //topicId
        startActivity(intent)
    }
}
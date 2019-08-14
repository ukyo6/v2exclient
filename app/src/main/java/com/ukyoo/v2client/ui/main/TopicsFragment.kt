package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.ui.detail.DetailActivity
import com.ukyoo.v2client.ui.userinfo.UserInfoActivity
import com.ukyoo.v2client.util.adapter.TopicListAdapter
import com.ukyoo.v2client.viewmodel.TopicsViewModel

/**
 * 单个主题列表页  (技术/创意/好玩...)
 */
class TopicsFragment : BaseFragment<FragmentTopicBinding>() {


    lateinit var topicsAdapter: TopicListAdapter

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

    private fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel

        topicsAdapter = TopicListAdapter(R.layout.item_topic)
        mBinding.recyclerView.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = topicsAdapter
        }

        topicsAdapter.setOnItemClickListener { _, _, position ->
            val item: TopicModel = topicsAdapter.data[position]
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra("topic_id", item.id)  //topicId
            startActivity(intent)
        }

        topicsAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.iv_avatar) {
                val item: TopicModel = topicsAdapter.data[position]
                val intent = Intent(mContext, UserInfoActivity::class.java)
                intent.putExtra("username", item.member?.username)
                startActivity(intent)
            }
        }

        //重试的回调
        mBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }


    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        initView()

        val nodeName = arguments?.getString(NODE_NAME)
        val tab = arguments?.getString(TAB_ID)

        if (nodeName != null) {
            viewModel.setNodeName(nodeName)
        } else if (tab != null) {
            viewModel.setNodeId(tab)
        }

        subscribeUi()
    }

    private fun subscribeUi(){
        //观察者
        viewModel.topics.observe(this@TopicsFragment, Observer { resource ->
            if (resource.data != null) {
                topicsAdapter.setNewData(resource.data)
            } else {
                topicsAdapter.setNewData(emptyList())
            }
        })
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

    /**
     * scrollToTop or refresh
     */
//    override fun toTopOrRefresh() {
//        if (mBinding.recyclerView.layoutManager is LinearLayoutManager) {
//            val layoutManager = mBinding.recyclerView.layoutManager as LinearLayoutManager
//            if (layoutManager.findLastVisibleItemPosition() > 5) {
//                mBinding.recyclerView.smoothScrollToPosition(0)
//            } else {
//                mBinding.recyclerView.smoothScrollToPosition(0)
//                loadData(true, null)
//            }
//        }
//    }


}
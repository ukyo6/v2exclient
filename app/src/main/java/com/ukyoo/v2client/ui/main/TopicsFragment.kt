package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
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

        //viewPager页面需要懒加载; 从nodes列表跳转过来直接加载
        lazyLoad = "lazyOpen".equals(arguments?.get(SOURCE))

        mBinding.vm = viewModel
        mBinding.recyclerView.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_topic, viewModel.list).apply {
                itemPresenter = this@TopicsFragment
            }
        }
        isPrepared = true
    }

    /**
     * get data from remote
     */
    override fun loadData(isRefresh: Boolean) {

        val topicId = arguments?.getString(NODE_ID)
        val tab = arguments?.getString(TAB_ID)


        if(topicId !=null){
            viewModel.name = topicId
            viewModel.loadDataByName(isRefresh = true)
                .bindLifeCycle(this)
                .subscribe({}, {
                    toastFailure(it)
                })
        } else if(tab != null){
            viewModel.tab = tab
            viewModel.loadDataByTab(isRefresh = true)
                .bindLifeCycle(this)
                .subscribe({}, {
                    toastFailure(it)
                })
        }
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

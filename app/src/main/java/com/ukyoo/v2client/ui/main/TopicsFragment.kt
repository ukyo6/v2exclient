package com.ukyoo.v2client.ui.main

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel
import com.ukyoo.v2client.util.bindLifeCycle
import java.util.function.Consumer

class TopicsFragment : BaseFragment<FragmentTopicBinding>() {

    private val viewModel by lazy {
        getInjectViewModel<TopicsViewModel>()
    }

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
    }

    override fun loadData(isRefresh: Boolean) {
        val topicType = arguments?.getString(TOPIC_TYPE)
        viewModel.topicId = topicType?:"11111"

        viewModel.loadData(isRefresh = true)
            .bindLifeCycle(this)
            .subscribe()


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

}

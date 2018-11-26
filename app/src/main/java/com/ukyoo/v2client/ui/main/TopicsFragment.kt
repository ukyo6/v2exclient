package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel

class TopicsFragment : BaseFragment<FragmentTopicBinding>() {

    val viewModel by lazy {
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
        val type = arguments?.get(TOPIC_TYPE)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

}

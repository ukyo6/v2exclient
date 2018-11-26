package com.ukyoo.v2client.ui.main

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentTopicBinding

class TopicsFragment : BaseFragment<FragmentTopicBinding>() {

    companion object {
        fun newInstance(bundle: Bundle):TopicsFragment{
            var fragment = TopicsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        getComponent().inject(this)
    }

    override fun loadData(isRefresh: Boolean) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_topic
    }

}

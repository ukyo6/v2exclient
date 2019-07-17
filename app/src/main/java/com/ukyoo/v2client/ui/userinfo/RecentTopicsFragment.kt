package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentTopicsBinding
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.viewmodel.RecentTopicsViewModel

class RecentTopicsFragment : BaseFragment<FragmentRecentTopicsBinding>() {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<RecentTopicsViewModel>()
    }

    companion object {
        fun newInstance(userName: String): RecentTopicsFragment {
            val fragment = RecentTopicsFragment()
            val bundle = Bundle()
            bundle.putString("userName",userName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        getComponent().inject(this)

        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
//            adapter = SingleTypeAdapter(mContext, R.layout.item_user_topic, viewModel.topic).apply {
//            }
        }
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
//        viewModel.getUserTopics(arguments?.getString("userName") ?: "")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_topics
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentRepliesBinding
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.viewmodel.RecentRepliesViewModel

class RecentRepliesFragment : BaseFragment<FragmentRecentRepliesBinding>() {
    companion object {
        fun newInstance(userName: String): RecentRepliesFragment {
            val fragment = RecentRepliesFragment()
            val bundle = Bundle()
            bundle.putString("userName", userName)
            fragment.arguments = bundle
            return fragment
        }
    }

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<RecentRepliesViewModel>()
    }


    override fun initView() {
        getComponent().inject(this)

        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_user_reply, viewModel.createdReplies)
        }

    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        viewModel.getUserReplies(arguments?.getString("userName") ?: "")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_replies
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
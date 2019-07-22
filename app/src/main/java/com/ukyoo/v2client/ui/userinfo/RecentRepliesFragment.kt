package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentRepliesBinding
import com.ukyoo.v2client.util.adapter.RepliesAdapter
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.viewmodel.RecentRepliesViewModel

/**
 * 用户信息
 * 最近的回复列表
 */
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

        repliesAdapter = RepliesAdapter(R.layout.item_user_reply)
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = repliesAdapter
        }
    }

    lateinit var repliesAdapter: RepliesAdapter

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {

        //设置参数
        viewModel.setUserNameAndPage((arguments?.getString("userName") ?: ""),1)
        //观察者
        viewModel.userReplies.observe(this@RecentRepliesFragment, Observer { list ->
            repliesAdapter.setNewData(list)
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_replies
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
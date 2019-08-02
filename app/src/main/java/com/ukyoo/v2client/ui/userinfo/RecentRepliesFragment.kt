package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentRepliesBinding
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.ui.main.TopicsFragment
import com.ukyoo.v2client.util.SizeUtils
import com.ukyoo.v2client.util.adapter.UserRepliesAdapter
import com.ukyoo.v2client.viewmodel.RecentRepliesViewModel
import com.ukyoo.v2client.widget.itemdecoration.LinearLayoutDecoration

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

        mBinding.vm = viewModel

        repliesAdapter = UserRepliesAdapter(R.layout.item_user_reply)
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = repliesAdapter
            addItemDecoration(
                LinearLayoutDecoration(
                    mContext, LinearLayout.VERTICAL,
                    SizeUtils.dp2px(mContext,1f), ContextCompat.getColor(mContext, R.color.black_12)
                )
            )
        }

        //重试的回调
        mBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }

    lateinit var repliesAdapter: UserRepliesAdapter

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {

        //设置参数
        viewModel.setUserNameAndPage((arguments?.getString("userName") ?: ""), 1)
        //观察者
        viewModel.userReplies.observe(this@RecentRepliesFragment, Observer { resource ->

            if (resource?.data == null) {
                repliesAdapter.setNewData(emptyList())
            } else {
                repliesAdapter.setNewData(resource.data)
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_replies
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
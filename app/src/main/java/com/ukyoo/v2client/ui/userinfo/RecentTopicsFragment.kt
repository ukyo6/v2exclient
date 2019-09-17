package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentTopicsBinding
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.util.SizeUtils
import com.ukyoo.v2client.util.adapter.UserTopicsAdapter
import com.ukyoo.v2client.viewmodel.RecentTopicsViewModel
import com.ukyoo.v2client.widget.itemdecoration.LinearLayoutDecoration

/**
 * 用户信息
 * 最近的主题列表
 */
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

    private fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel

        repliesAdapter = UserTopicsAdapter(R.layout.item_user_topic)
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

    lateinit var repliesAdapter: UserTopicsAdapter

    override fun loadData(isRefresh: Boolean) {
        initView()

        //设置参数
        viewModel.setUserNameAndPage((arguments?.getString("userName") ?: ""),1)
        //观察者
        viewModel.userTopics.observe(this@RecentTopicsFragment, Observer { resource ->

            if(resource?.data == null){
                repliesAdapter.setNewData(emptyList())
            } else {
                repliesAdapter.setNewData(resource.data)
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_topics
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentRecentTopicsBinding
import com.ukyoo.v2client.util.adapter.UserTopicsAdapter
import com.ukyoo.v2client.viewmodel.RecentTopicsViewModel

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

    override fun initView() {
        getComponent().inject(this)

        repliesAdapter = UserTopicsAdapter(R.layout.item_user_topic)
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = repliesAdapter
        }
    }

    lateinit var repliesAdapter: UserTopicsAdapter

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        //设置参数
        viewModel.setUserNameAndPage((arguments?.getString("userName") ?: ""),1)
        //观察者
        viewModel.userTopics.observe(this@RecentTopicsFragment, Observer { list ->
            repliesAdapter.setNewData(list)
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recent_topics
    }

    override fun isLazyLoad(): Boolean {
        return true
    }
}
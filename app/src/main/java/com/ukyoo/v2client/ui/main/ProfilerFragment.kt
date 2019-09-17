package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.databinding.FragmentPersonalBinding
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.inter.ToTopOrRefreshContract
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.ui.userinfo.RecentRepliesFragment
import com.ukyoo.v2client.ui.userinfo.RecentTopicsFragment
import com.ukyoo.v2client.util.RxBus
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.adapter.BaseViewPagerAdapter
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodel.ProfilerViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 个人中心
 */
class ProfilerFragment : BaseFragment<FragmentPersonalBinding>() {

    companion object {
        fun newInstance(): ProfilerFragment {
            return ProfilerFragment()
        }
    }

    private val viewModel by lazy { getInjectViewModel<ProfilerViewModel>() }

    override fun isLazyLoad(): Boolean = true

    private fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)
    }

    override fun loadData(isRefresh: Boolean) {
        initView()

        //登录后返回个人信息页
        RxBus.toFlowable(ProfileModel::class.java)
            .bindLifeCycle(this)
            .subscribe {
                viewModel.setUserProfiler(it)
            }

        //跳转登录页
        mBinding.ivAvatar.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        //刷新个人信息
        viewModel.refreshProfiler()

        subScribeUi()
    }

    private fun subScribeUi() {
        viewModel.userProfilerLiveData.observe(this@ProfilerFragment, Observer { it ->
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    it.data?.let { userInfo ->
                        val tabTitles = arrayOf("创建的主题", "最近的回复")
                        mBinding.viewpager.apply {
                            adapter = object : BaseViewPagerAdapter(childFragmentManager, tabTitles) {
                                override fun getItem(pos: Int): Fragment? {
                                    return when (pos) {
                                        0 -> RecentTopicsFragment.newInstance(userInfo.username)
                                        1 -> RecentRepliesFragment.newInstance(userInfo.username)
                                        else -> null
                                    }
                                }
                            }
                            offscreenPageLimit = (adapter as BaseViewPagerAdapter).count - 1
                        }
                    }

                    mBinding.tabLayout.apply {
                        setupWithViewPager(mBinding.viewpager)

                        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabReselected(p0: TabLayout.Tab?) {
                                //refresh topicFragment
                                val abstractPagerAdapter = viewpager.adapter as BaseViewPagerAdapter
                                abstractPagerAdapter.getItem(mBinding.viewpager.currentItem).let {
                                    if (it is ToTopOrRefreshContract) {
                                        it.toTopOrRefresh()
                                    }
                                }
                            }

                            override fun onTabUnselected(p0: TabLayout.Tab?) {
                            }

                            override fun onTabSelected(p0: TabLayout.Tab?) {
                            }
                        })
                    }
                }
                Status.ERROR -> {
                    ToastUtil.shortShow(it.message)
                }

            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }


    //退出登录
    fun signOut() {
        NetManager.clearCookie()
    }

}
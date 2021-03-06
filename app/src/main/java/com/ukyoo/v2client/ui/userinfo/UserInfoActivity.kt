package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityUserinfoBinding
import com.ukyoo.v2client.inter.ToTopOrRefreshContract
import com.ukyoo.v2client.util.adapter.BaseViewPagerAdapter
import com.ukyoo.v2client.viewmodel.UserInfoViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 *  用户信息界面
 */
class UserInfoActivity : BaseActivity<ActivityUserinfoBinding>(){

    private var mUsername: String = ""

    private val viewModel by lazy { getInjectViewModel<UserInfoViewModel>() }


    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_userinfo
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val intent = intent
            val data = intent.data
            val scheme = if (data != null) data.scheme else "" // "http"
            val host = if (data != null) data.host else "" // "www.v2ex.com"
            val params = data?.pathSegments
            if ((scheme == "http" || scheme == "https")
                && (host == "www.v2ex.com" || host == "v2ex.com")
                && params != null && params.size == 2
            ) {
                mUsername = params[1]
                updateTitle(mUsername)
            } else {
                mUsername = intent.getStringExtra("username")
                updateTitle(mUsername)
            }
        } else {
            mUsername = savedInstanceState.getString("username") ?: ""
        }

        val tabTitles = arrayOf("创建的主题", "最近的回复")
        mBinding.viewpager.apply {
            adapter = object : BaseViewPagerAdapter(supportFragmentManager, tabTitles) {
                override fun getItem(pos: Int): Fragment? {
                    return when (pos) {
                        0 -> RecentTopicsFragment.newInstance(mUsername)
                        1 -> RecentRepliesFragment.newInstance(mUsername)
                        else -> null
                    }
                }
            }
            offscreenPageLimit = (adapter as BaseViewPagerAdapter).count - 1
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

        //根据userName查询信息
        viewModel.setUserName(mUsername)
    }

    private fun updateTitle(username: String) {
//        mBinding.toolbar.title = username
    }

}
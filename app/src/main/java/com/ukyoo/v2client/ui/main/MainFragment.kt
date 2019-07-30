package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainBinding
import com.ukyoo.v2client.util.adapter.BaseViewPagerAdapter
import javax.inject.Inject

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun isLazyLoad(): Boolean {
        return false
    }

    @Inject
    lateinit var manager: FragmentManager

    override fun initView() {
        //inject
        getComponent().inject(this)
        //初始化viewpager
        mBinding.viewpager.run {
            adapter = object : BaseViewPagerAdapter(manager, arrayOf("1", "2", "3")) {
                override fun getItem(pos: Int): Fragment? {
                    when (pos) {
                        0 -> list[0] = HomeFragment.newInstance(Bundle())
                        1 -> list[1] = NodesFragment.newInstance(Bundle())
                        2 -> list[2] = ProfilerFragment.newInstance(Bundle())
                    }
                    return list[pos]
                }
            }
            offscreenPageLimit = (adapter as BaseViewPagerAdapter).count - 1
        }

        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> mBinding.navigationView.selectedItemId = R.id.nav_home  //主页
                    1 -> mBinding.navigationView.selectedItemId = R.id.nav_repos //节点
                    2 -> mBinding.navigationView.selectedItemId = R.id.nav_profile //个人
                }
            }
        })

        mBinding.navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    mBinding.viewpager.setCurrentItem(0, false)
                }
                R.id.nav_repos -> {
                    mBinding.viewpager.setCurrentItem(1, false)
                }
                R.id.nav_profile -> {
                    mBinding.viewpager.setCurrentItem(2, false)
                }
            }
            true
        }
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }
}
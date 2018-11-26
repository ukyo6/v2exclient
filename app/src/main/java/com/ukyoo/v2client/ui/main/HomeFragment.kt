package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentHomeBinding
import com.ukyoo.v2client.ui.viewmodels.HomeViewModel
import com.ukyoo.v2client.util.adapter.AbstractPagerAdapter
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var manager: FragmentManager

    companion object {
        fun newInstance(bundle: Bundle): HomeFragment {
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    private val viewModel: HomeViewModel by lazy {
        getInjectViewModel<HomeViewModel>()
    }

    override fun initView() {
        getComponent().inject(this)

        val tabTitles = mContext.resources.getStringArray(R.array.v2ex_favorite_tab_titles)
        val TabPaths = mContext.resources.getStringArray(R.array.v2ex_favorite_tab_paths)
        val mFragments = ArrayList<Fragment>()
        for (tabTitle in tabTitles) {
            val fragment = TopicsFragment()
            mFragments.add(fragment)
        }

        val viewPager = mBinding.viewpager
        viewPager.offscreenPageLimit = mFragments.size - 1
        viewPager.adapter = object : AbstractPagerAdapter(manager, tabTitles) {
            override fun getItem(pos: Int): Fragment? {
                when(pos){
                    pos -> list[pos] = mFragments[pos]
                }
                return list[pos]
            }
        }
        mBinding.tabLayout.setupWithViewPager(viewPager)
    }

    override fun loadData(isRefresh: Boolean) {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


}
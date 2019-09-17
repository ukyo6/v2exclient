package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentHomeBinding
import com.ukyoo.v2client.util.adapter.BaseViewPagerAdapter
import javax.inject.Inject

/**
 *  首页, 包含最新,最热节点
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun isLazyLoad() = true

    @Inject
    lateinit var manager: FragmentManager

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun loadData(isRefresh: Boolean) {

        //inject
        getComponent().inject(this)
        //set viewpager and tablayout
        val tabTitles = resources.getStringArray(R.array.v2ex_favorite_tab_titles)
        val tabPaths = resources.getStringArray(R.array.v2ex_favorite_tab_paths)

        mBinding.viewpager.apply {
            adapter = object : BaseViewPagerAdapter(manager, tabTitles) {
                override fun getItem(pos: Int): Fragment? {
                    when (pos) {
//                    0 -> list[0] = TopicListFragment.newInstance("latest")  //最新
//                    1 -> list[1] = TopicListFragment.newInstance("hot")   //最热
                        pos -> {
                            val fragment = TopicsFragment.newInstance(
                                bundle = Bundle().apply { putString(TopicsFragment.TAB_ID, tabPaths[pos]) },
                                source = "lazyOpen"
                            )
                            list[pos] = fragment
                        }
                    }
                    return list[pos]
                }
            }
            offscreenPageLimit = (adapter as BaseViewPagerAdapter).count - 1
        }

        mBinding.tabLayout.apply {
            setupWithViewPager(mBinding.viewpager)

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    //refresh topicFragment
//                    val abstractPagerAdapter = viewpager.adapter as BaseViewPagerAdapter
//                    abstractPagerAdapter.getItem(mBinding.viewpager.currentItem).let {
//                        if (it is ToTopOrRefreshContract) {
//                            it.toTopOrRefresh()
//                        }
//                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                }
            })
        }
    }
}
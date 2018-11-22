package com.ukyoo.v2client.ui.main

import androidx.fragment.app.Fragment
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainBinding
import com.ukyoo.v2client.ui.viewmodels.HomeViewModel
import java.util.ArrayList

class HomeFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: HomeViewModel by lazy {
        getInjectViewModel<HomeViewModel>()
    }

    override fun loadData(isRefresh: Boolean) {
        val tabTitles = mContext.resources.getStringArray(R.array.v2ex_favorite_tab_titles)
        val TabPaths = mContext.resources.getStringArray(R.array.v2ex_favorite_tab_paths)

        val mFragments = ArrayList<Fragment>()
        for (tabTitle in tabTitles) {
            val fragment = TopicsFragment()
            mFragments.add(fragment)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        getComponent().inject(this)
    }
}
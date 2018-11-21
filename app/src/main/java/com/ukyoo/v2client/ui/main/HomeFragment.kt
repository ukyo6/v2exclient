package com.ukyoo.v2client.ui.main

import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainBinding
import com.ukyoo.v2client.ui.viewmodels.HomeViewModel

class HomeFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: HomeViewModel by lazy {
        getInjectViewModel<HomeViewModel>()
    }

    override fun loadData(isRefresh: Boolean) {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        getComponent().inject(this)
    }
}
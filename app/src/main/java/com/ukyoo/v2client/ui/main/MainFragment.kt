package com.ukyoo.v2client.ui.main

import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainBinding
import com.ukyoo.v2client.viewmodels.MainViewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by lazy {
        getInjectViewModel<MainViewModel>()
    }



    override fun loadData(isRefresh: Boolean) {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
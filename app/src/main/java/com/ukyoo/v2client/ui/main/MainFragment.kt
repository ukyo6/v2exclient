
package com.ukyoo.v2client.ui.main

import androidx.fragment.app.Fragment
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainBinding
import java.util.*

class MainFragment: BaseFragment<FragmentMainBinding>() {

    override fun initView() {



        val viewpager = mBinding.viewpager

        mBinding.navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    viewpager.currentItem =0
                }
                R.id.nav_repos ->{
                    viewpager.currentItem=1
                }
                R.id.nav_profile->{
                    viewpager.currentItem=2
                }
            }
            true
        }
    }

    override fun loadData(isRefresh: Boolean) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }
}
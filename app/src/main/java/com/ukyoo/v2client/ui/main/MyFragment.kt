package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMyBinding
import com.ukyoo.v2client.ui.login.LoginActivity

/**
 * 个人中心
 */
class MyFragment: BaseFragment<FragmentMyBinding>() {
    companion object {
        fun newInstance(bundle: Bundle):MyFragment{
            val meFragment = MyFragment()
            meFragment.arguments = bundle
            return meFragment
        }
    }

    override fun initView() {
        mBinding.btnGotologin.setOnClickListener{
            startActivity(Intent(mContext,LoginActivity::class.java))
        }
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }
}
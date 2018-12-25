package com.ukyoo.v2client.ui.login

import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityLoginBinding
import com.ukyoo.v2client.ui.viewmodels.LoginViewModel

/**
 * 登录
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<LoginViewModel>()
    }


    override fun loadData(isRefresh: Boolean) {
        //登录需要的信息在首页里
        viewModel.signin()
    }

    override fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }
}
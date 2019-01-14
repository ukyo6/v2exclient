package com.ukyoo.v2client.ui.login

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityLoginBinding
import com.ukyoo.v2client.event.LoginSuccessEvent
import com.ukyoo.v2client.navigator.LoginNavigator
import com.ukyoo.v2client.util.RxBus
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.viewmodels.LoginViewModel

/**
 * 登录
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginNavigator {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<LoginViewModel>()
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        viewModel.setLoginNavigator(this@LoginActivity)

        viewModel.getLoginData()
    }

    override fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    //-----------------------------------------------------------------------------------------------


    //登录成功
    override fun loginSuccess() {
        SPUtils.setBoolean("isLogin", true)
        finish()
        RxBus.getDefault()
            .post(LoginSuccessEvent())
    }
}
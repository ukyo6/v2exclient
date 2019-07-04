package com.ukyoo.v2client.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.databinding.ActivityLoginBinding
import com.ukyoo.v2client.event.LoginSuccessEvent
import com.ukyoo.v2client.navigator.LoginNavigator
import com.ukyoo.v2client.util.RxBus

/**
 * 登录
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginNavigator {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<LoginViewModel>()
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        viewModel.apply {
            //登录成功的回调
            loginSuccessEvent.observe(this@LoginActivity, Observer {
                loginSuccess(it)
            })
        }

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
    override fun loginSuccess(model: ProfileModel) {
        RxBus.default.post(LoginSuccessEvent(model))
        finish()
    }
}
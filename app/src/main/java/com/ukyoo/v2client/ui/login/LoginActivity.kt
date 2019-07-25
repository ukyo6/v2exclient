package com.ukyoo.v2client.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.data.Status
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
        //刷新验证码
        viewModel.refreshVerifyImg()

        //登录成功获取个人信息 观察者
        viewModel.userProfiler.observe(this@LoginActivity, Observer {
            if (it.status == Status.SUCCESS && it?.data != null) {
                finish()
            }
        })
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
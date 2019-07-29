package com.ukyoo.v2client.ui.login

import android.content.Intent
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
import com.ukyoo.v2client.util.setUpToast

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

        //Toast 观察者
        setUpToast(this@LoginActivity, viewModel.toastEvent)

        //跳转个人信息页 观察者
        viewModel.loginEvent.observe(this@LoginActivity, Observer {
//            RxBus.default.post(it.data)
            finish()
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
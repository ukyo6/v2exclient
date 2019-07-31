package com.ukyoo.v2client.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.databinding.ActivityLoginBinding
import com.ukyoo.v2client.util.*

/**
 * 登录
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<LoginViewModel>()
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        //刷新验证码
        if (viewModel.verifyImgUrlLiveData.value == null) {
            viewModel.clickRefreshVerifyImg()
        }

        mBinding.btnLogin.setOnClickListener {
            val userNameVal = mBinding.tvUsername.text.toString()
            val pwdVal = mBinding.tvPwd.text.toString()
            val verifyCodeVal = mBinding.tvVerify.text.toString()

            when {
                userNameVal.isBlank() -> ToastUtil.shortShow("请输入用户名")
                pwdVal.isBlank() -> ToastUtil.shortShow("请输入密码")
                verifyCodeVal.isBlank() -> ToastUtil.shortShow("请输入验证码")
                else -> {
                    viewModel.setLogin(LoginViewModel.LoginParam(userNameVal, pwdVal, verifyCodeVal))
                }
            }
        }


        subscribeUi()
    }

    private fun subscribeUi() {

        mBinding.ivVerifycode.setOnClickListener {
            viewModel.clickRefreshVerifyImg()
        }

        viewModel.verifyImgUrlLiveData.observe(this@LoginActivity, Observer {
            //展示验证码
        })


        //跳转个人信息页
        viewModel.loginResultLiveData.observe(this@LoginActivity, Observer {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> ToastUtil.shortShow(it.message)
                Status.EMPTY ->{}
                Status.SUCCESS -> {
                    it.data?.let { it1 ->
                        RxBus.post(it1)
                        finish()
                    }
                }
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
}
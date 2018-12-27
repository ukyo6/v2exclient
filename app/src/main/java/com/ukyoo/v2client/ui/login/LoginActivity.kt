package com.ukyoo.v2client.ui.login

import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityLoginBinding
import com.ukyoo.v2client.ui.A
import com.ukyoo.v2client.ui.viewmodels.LoginViewModel
import com.ukyoo.v2client.util.GlideApp
import java.util.concurrent.ThreadPoolExecutor

/**
 * 登录
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<LoginViewModel>()
    }


    override fun loadData(isRefresh: Boolean) {
    }

    override fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel.apply {
            initViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }
}
package com.ukyoo.v2client.ui.main

import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityMainBinding
import com.ukyoo.v2client.ui.viewmodels.MainViewModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.util.toast
import io.reactivex.Single


/**
 * 主页
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var isQuit = false

    override fun loadData(isRefresh: Boolean) {


    }

    override fun initView() {
        getComponent().inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private val viewModel  by lazy {
        getInjectViewModel<MainViewModel>()
    }

    override fun onBackPressed() {
        if (!isQuit) {
            toast(msg = "再按一次退出程序")
            isQuit = true
            //在两秒钟之后isQuit会变成false
            Single.just(isQuit)
                .async(2000)
                .bindLifeCycle(this)
                .subscribe({ isQuit = false }, {})
        } else {
            super.onBackPressed()
        }

    }
}

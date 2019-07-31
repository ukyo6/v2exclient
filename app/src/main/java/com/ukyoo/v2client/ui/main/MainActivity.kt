package com.ukyoo.v2client.ui.main

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityMainBinding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import io.reactivex.Single


/**
 * 主页
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
    }

    private var isQuit = false

    override fun initView() {
        getComponent().inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        if (!isQuit) {
            ToastUtil.shortShow("再按一次退出程序")
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

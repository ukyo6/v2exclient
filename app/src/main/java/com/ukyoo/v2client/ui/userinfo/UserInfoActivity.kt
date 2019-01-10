package com.ukyoo.v2client.ui.userinfo

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityUserinfoBinding
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.viewmodels.UserInfoViewModel

class UserInfoActivity : BaseActivity<ActivityUserinfoBinding>() {
    private lateinit var mMember: MemberModel
    private var mUsername: String = ""

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<UserInfoViewModel>()
    }


    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_userinfo
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            /**
             * deal such scheme: [go](http://www.v2ex.com/member/njustyw)>
             *
             * AndroidMainfext.xml config:
             * <data android:scheme="http" android:host="www.v2ex.com" android:pathPattern="/member/.*"></data>
             */
            val intent = intent
            val data = intent.data
            val scheme = if (data != null) data.scheme else "" // "http"
            val host = if (data != null) data.host else "" // "www.v2ex.com"
            val params = data?.pathSegments
            if ((scheme == "http" || scheme == "https")
                && (host == "www.v2ex.com" || host == "v2ex.com")
                && params != null && params.size == 2
            ) {
                mUsername = params[1]
                updateTitle(mUsername)
            } else {
                if (intent.hasExtra("model")) {
                    mMember = intent.getParcelableExtra<MemberModel>("model")
                    mUsername = mMember.username
                    updateTitle(mUsername)
                } else {
                    mUsername = intent.getStringExtra("username")
                    updateTitle(mUsername)
                }
            }
        } else {
            mUsername = savedInstanceState.getString("username") ?: ""
        }

        viewModel.getUserInfo(mUsername, isRefresh = true)
    }

    private fun updateTitle(username: String) {
        mBinding.toolbar.title = username
    }
}
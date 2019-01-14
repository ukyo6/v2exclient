package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentPersonalBinding
import com.ukyoo.v2client.event.LoginSuccessEvent
import com.ukyoo.v2client.navigator.PersonalNavigator
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.util.RxBus
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodels.PersonalViewModel

/**
 * 个人中心
 */
class PersonalFragment : BaseFragment<FragmentPersonalBinding>(),PersonalNavigator {

    companion object {
        fun newInstance(bundle: Bundle): PersonalFragment {
            val meFragment = PersonalFragment()
            meFragment.arguments = bundle
            return meFragment
        }
    }

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<PersonalViewModel>()
    }

    override fun initView() {

    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        //登录后获取用户信息
        RxBus.getDefault()
            .toFlowable(LoginSuccessEvent::class.java)
            .bindLifeCycle(this)
            .subscribe {
                viewModel.getUserProfiler()
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun gotoLogin() {
        //未登录就跳转到登录界面
        if (SPUtils.getBoolean("isLogin", false)) {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}
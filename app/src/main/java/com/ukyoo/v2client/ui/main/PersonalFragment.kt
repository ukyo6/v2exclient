package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentPersonalBinding
import com.ukyoo.v2client.db.AppDataBase
import com.ukyoo.v2client.event.LoginSuccessEvent
import com.ukyoo.v2client.navigator.PersonalNavigator
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.util.RxBus
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodels.PersonalViewModel

/**
 * 个人中心
 */
class PersonalFragment : BaseFragment<FragmentPersonalBinding>(), PersonalNavigator {
    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun gotoLogin() {
        startActivity(Intent(activity, LoginActivity::class.java))
    }

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
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        viewModel.apply {

            viewModel.getUserInfoCache()

            hasLogin.observe(this@PersonalFragment, Observer { isLogin ->
                if (!isLogin) {
                    gotoLogin()
                }
            })
        }

        //登录后返回
        RxBus.getDefault()
            .toFlowable(LoginSuccessEvent::class.java)
            .bindLifeCycle(this)
            .subscribe {
                viewModel.setUserProfiler(it.model)
            }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }
}
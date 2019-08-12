package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.databinding.FragmentPersonalBinding
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.navigator.PersonalNavigator
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.util.RxBus
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodel.ProfilerViewModel

/**
 * 个人中心
 */
class ProfilerFragment : BaseFragment<FragmentPersonalBinding>(), PersonalNavigator {

    companion object {
        fun newInstance(bundle: Bundle): ProfilerFragment {
            val meFragment = ProfilerFragment()
            meFragment.arguments = bundle
            return meFragment
        }
    }

    private val viewModel by lazy { getInjectViewModel<ProfilerViewModel>() }

    override fun isLazyLoad(): Boolean = true

    override fun gotoLogin() {
        startActivity(Intent(activity, LoginActivity::class.java))
    }


    private fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        //跳转登录页
        mBinding.ivAvatar.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        initView()
        //登录后返回
        RxBus.toFlowable(ProfileModel::class.java)
            .bindLifeCycle(this)
            .subscribe {
                viewModel.setUserProfiler(it)
            }

        subScribeUi()
    }

    private fun subScribeUi() {
        viewModel.userProfilerLiveData.observe(this@ProfilerFragment, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {

                }
                Status.ERROR -> {
                    ToastUtil.shortShow(it.message)
                }
            }
        })

        mBinding.ivAvatar.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }
}
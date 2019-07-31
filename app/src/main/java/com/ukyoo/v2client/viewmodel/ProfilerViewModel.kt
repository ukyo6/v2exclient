package com.ukyoo.v2client.viewmodel

import com.ukyoo.v2client.App
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.repository.ProfilerRepository
import com.ukyoo.v2client.util.SPUtils
import javax.inject.Inject

class ProfilerViewModel @Inject constructor(private val repository: ProfilerRepository) : AutoDisposeViewModel() {


    val userProfilerLiveData = repository.getUserProfiler()

    /**
     * 登录
     */
    fun callToLogin() {

    }




    /**
     * 退出登录
     */
    fun exitLogin() {
        NetManager.clearCookie()

        SPUtils.setBoolean("isLogin", false)
        val model = ProfileModel()
        model.username = App.instance().getString(R.string.please_login)
        model.avatar = ""
//        profileModel.setValue(null)
    }

    /**
     * 我的回复
     */
    fun gotoUserRepliesPage() {

    }

    /**
     * 我发表的主题
     */
    fun gotoUserTopicsPage() {

    }
}
package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableField
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.db.AppDataBase
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.SingleLiveEvent
import com.ukyoo.v2client.util.async
import javax.inject.Inject

class PersonalViewModel @Inject constructor() : BaseViewModel() {

    var profileModel = ObservableField<ProfileModel>()

    var hasLogin = SingleLiveEvent<Boolean>()

    fun setUserProfiler(model: ProfileModel) {
        profileModel.set(model)
    }

    /**
     * 获取之前登录被缓存的用户信息
     */
    fun getUserInfoCache() {
        if (SPUtils.getBoolean("isLogin", false)) {
            AppDataBase.getDataBase()
                .profileModelDao()
                .getUserProfile()
                .async()
                .subscribe { model ->
                    profileModel.set(model)
                }
        }
    }

    /**
     * 登录
     */
    fun callToLogin() {
        if (!SPUtils.getBoolean("isLogin", false)) {
            hasLogin.value = false
        }
    }

    /**
     * 退出登录
     */
    fun exitLogin() {
        NetManager.clearCookie()

        SPUtils.setBoolean("isLogin", false)
        val model = ProfileModel()
        model.username = App.instance().getString(R.string.please_login)
        model.avatar = null
        profileModel.set(model)
    }

    /**
     * 我的回复
     */
    fun gotoUserRepliesPage(){

    }

    /**
     * 我发表的主题
     */
    fun gotoUserTopicsPage(){

    }
}
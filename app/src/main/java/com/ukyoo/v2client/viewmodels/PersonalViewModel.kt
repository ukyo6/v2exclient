package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableField
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.db.AppDataBase
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.SingleLiveEvent
import com.ukyoo.v2client.util.async
import javax.inject.Inject
import javax.inject.Named

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
        if (SPUtils.getBoolean("isLoin", false)) {
            AppDataBase.getDataBase()
                .profileModelDao()
                .getUserProfile()
                .async()
                .subscribe { model ->
                    profileModel.set(model)
                }
        }
    }

    fun callToLogin() {
        if (!SPUtils.getBoolean("isLoin", false)) {
            hasLogin.value = false
        }
    }
}
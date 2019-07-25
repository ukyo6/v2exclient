package com.ukyoo.v2client.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.util.ToastUtil
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : BaseViewModel() {

    val username = ObservableField<String>()
    val password = ObservableField<String>()
    val verifyCode = ObservableField<String>()

    /**
     * 登录
     */
    fun clickToLogin() {
        val userNameVal = username.get()
        val pwdVal = password.get()
        val verifyCodeVal = verifyCode.get()
        when {
            userNameVal.isNullOrBlank() -> {
                ToastUtil.shortShow("请输入用户名")
            }
            pwdVal.isNullOrBlank() -> {
                ToastUtil.shortShow("请输入密码")
            }
            verifyCodeVal.isNullOrBlank() -> {
                ToastUtil.shortShow("请输入验证码")
            }
            else -> {
                repository.login(userNameVal, pwdVal, verifyCodeVal)
            }
        }
    }

    //刷新验证码
    private val refreshVerifyEvent = MutableLiveData<Boolean>()

    fun refreshVerifyImg() {
        refreshVerifyEvent.value = true
    }

    //验证码地址
    val verifyImgUrl: LiveData<Resource<String>> = Transformations.switchMap(refreshVerifyEvent) {
        repository.getLoginData()
    }


    //用户信息(节点收藏 主题收藏 特别关注)
    val userProfiler: LiveData<Resource<ProfileModel>> = MutableLiveData()


}
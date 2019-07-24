package com.ukyoo.v2client.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.util.SingleLiveEvent
import com.ukyoo.v2client.util.ToastUtil
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel() {


    val username = ObservableField<String>()
    val password = ObservableField<String>()
    val verifyCode = ObservableField<String>()


    //liveData
    val loginSuccessEvent = SingleLiveEvent<ProfileModel>()

    /**
     * 登录
     */
    fun login() {
        when {
            username.get().isNullOrBlank() -> {
                ToastUtil.shortShow("请输入用户名")
            }
            password.get().isNullOrBlank() -> {
                ToastUtil.shortShow("请输入密码")
            }
            verifyCode.get().isNullOrBlank() -> {
                ToastUtil.shortShow("请输入验证码")
            }
            else -> {
                repository.login(username.get()!!, password.get()!!, verifyCode.get()!!)
            }
        }
    }


    //验证码地址
    var verifyImgUrl: LiveData<Resource<String>> = refreshVerifyImg()

    fun refreshVerifyImg() : LiveData<Resource<String>>{
        return  repository.getLoginData()
    }
}
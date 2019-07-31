package com.ukyoo.v2client.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.util.Event
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : AutoDisposeViewModel() {

    //Toast
    private val _toastEvent = MutableLiveData<Event<String>>()
    val toastEvent: LiveData<Event<String>> = _toastEvent

    //登录参数
    private val _loginParam = MutableLiveData<LoginParam>()

    fun setLogin(param: LoginParam) {
        _loginParam.value = param
    }

    //登录结果
    val loginResultLiveData: LiveData<Resource<ProfileModel>> = Transformations.switchMap(_loginParam) {
        repository.login(it)
    }


    //验证码
    private val _refreshVerifyEvent = MutableLiveData<Unit>()

    fun clickRefreshVerifyImg() {
        _refreshVerifyEvent.value = Unit
    }

    val verifyImgUrlLiveData: LiveData<Resource<String>> = Transformations.switchMap(_refreshVerifyEvent) {
        repository.getVerifyUrl()
    }

    data class LoginParam(
        val userName: String,
        val pwd: String,
        val verifyCode: String
    )
}
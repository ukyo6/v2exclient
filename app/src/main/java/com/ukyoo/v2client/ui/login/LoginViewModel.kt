package com.ukyoo.v2client.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.util.Event
import io.reactivex.Flowable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: LoginRepository) : AutoDisposeViewModel() {

    val username = ObservableField<String>()
    val password = ObservableField<String>()
    val verifyCode = ObservableField<String>()

    //Toast
    private val _toastEvent = MutableLiveData<Event<String>>()
    val toastEvent: LiveData<Event<String>> = _toastEvent

    //UserProfiler
    val loginEvent = MutableLiveData<Resource<ProfileModel>>()

    /**
     * 登录
     */
    fun clickToLogin() {
        val userNameVal = username.get()
        val pwdVal = password.get()
        val verifyCodeVal = verifyCode.get()



        when {
            userNameVal.isNullOrBlank() -> _toastEvent.value = Event("请输入用户名")
            pwdVal.isNullOrBlank() ->  _toastEvent.value = Event("请输入密码")
            verifyCodeVal.isNullOrBlank() -> _toastEvent.value = Event("请输入验证码")
            else -> {
                loginEvent.value = repository.login(userNameVal, pwdVal, verifyCodeVal).value
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

    fun hehe(){

        LiveDataReactiveStreams.fromPublisher(Flowable.just(2))
    }



}
package com.ukyoo.v2client.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.Event
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
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

    /**
     * 登录结果
     */
    val loginResultLiveData: LiveData<Resources<ProfileModel>> = Transformations.switchMap(_loginParam) {
        login(it)
    }

    private fun login(param: LoginParam): LiveData<Resources<ProfileModel>> {
        val result = MutableLiveData<Resources<ProfileModel>>()

        repository.login(param)
            .async()
            .autoDisposable(this)
            .subscribe(
                { data ->
                    result.value = Resources.success(data)
                },
                { error ->
                    result.value = Resources.error(ErrorHanding.handleError(error))
                }
            )
        return result
    }




    /**
     * 验证码
     */
    val verifyImgUrlLiveData = MutableLiveData<Resources<String>>()

    fun getVerifyUrl() {
        repository.getVerifyUrl()
            .async()
            .doOnSubscribe { }
            .autoDisposable(this)
            .subscribe(
                { data ->
                    verifyImgUrlLiveData.setValue(Resources.success(data))
                },
                { error ->
                    val errMsg = ErrorHanding.handleError(error)
                    ToastUtil.shortShow(errMsg)
                    verifyImgUrlLiveData.setValue(Resources.error(errMsg))
                }
            )
    }

    data class LoginParam(
        val userName: String,
        val pwd: String,
        val verifyCode: String
    )
}


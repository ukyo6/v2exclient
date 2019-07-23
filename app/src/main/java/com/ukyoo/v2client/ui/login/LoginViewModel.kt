package com.ukyoo.v2client.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.db.AppDataBase
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.repository.LoginRepository
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.*
import org.jsoup.Jsoup
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel() {


    val username = ObservableField<String>()
    val password = ObservableField<String>()
    val verifyCode = ObservableField<String>()

    //验证码地址
    lateinit var verifyImgUrl: MutableLiveData<String>

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


    /**
     * 刷新验证码
     */
    fun refreshVerifyImg() {
        verifyImgUrl = repository.getLoginData()
    }
}
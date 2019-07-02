package com.ukyoo.v2client.ui.login

import androidx.databinding.ObservableField
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.db.AppDataBase
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.util.*
import org.jsoup.Jsoup
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel @Inject constructor(
    @Named("cached") private var htmlService: HtmlService
) : BaseViewModel() {
    var username = ObservableField<String>()
    var password = ObservableField<String>()
    var verifyCode = ObservableField<String>()
    var verifyImgUrl = ObservableField<String>()

    //请求参数的key
    private var nameVal: String = ""
    private var passwordVal: String = ""
    private var verifyCodeVal: String = ""
    private var once: String = ""


    //liveData
    var loginSuccessEvent = SingleLiveEvent<ProfileModel>()

    /**
     *  从首页获取登录需要的信息
     */
    fun getLoginData() {
        NetManager.clearCookie()

        htmlService.signin()
            .async()
            .subscribe({ content ->
                //从响应头里获取cookie信息
                val headerCookies = content.headers().values("Set-Cookie")
                val cookies = HashSet<String>()
                if (headerCookies != null && !headerCookies.isEmpty()) {
                    headerCookies.forEach {
                        cookies.add(it)
                    }
                }
                SPUtils.setStringSet("cookie", cookies)

                if (content?.body() == null) {
                    return@subscribe
                }
                val body = Jsoup.parse(content.body())
                val boxes = body.getElementsByClass("box")
                for (el in boxes) {
                    val cell = el.getElementsByClass("cell")
                    for (c in cell) {
                        nameVal = c.getElementsByAttributeValue("type", "text").attr("name")
                        passwordVal = c.getElementsByAttributeValue("type", "password").attr("name")
                        once = c.getElementsByAttributeValue("name", "once").attr("value")
                        if (nameVal.isEmpty() || passwordVal.isEmpty()) {
                            continue
                        }

                        verifyCodeVal = c.getElementsByAttributeValue("type", "text")[1].attr("name")
                        val verifyStr = c.getElementsByAttributeValueContaining("style", "background-image").toString()
                        val startIndex = verifyStr.indexOf("/_captcha?")
                        val endIndex = verifyStr.indexOf("\')")
                        verifyImgUrl.set("https://www.v2ex.com" + verifyStr.substring(startIndex, endIndex))
                        break
                    }
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

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
                val params = HashMap<String, String>()
                params.put(nameVal, username.get() ?: "")
                params.put("once", once)
                params.put(passwordVal, password.get() ?: "")
                params.put(verifyCodeVal, verifyCode.get() ?: "")


                val headers = HashMap<String, String>()
                val stringSet = SPUtils.getStringSet("cookie")
                val stringBuffer = StringBuffer()
                stringSet?.forEach {
                    if (stringSet.indexOf(it) != stringSet.size - 1) {
                        stringBuffer.append(it).append("; ")
                    } else {
                        stringBuffer.append(it)
                    }
                }

                headers.put("Origin", "https://www.v2ex.com")
                headers.put("Referer", "https://www.v2ex.com/signin")
                headers.put("Content-Type", "application/x-www-form-urlencoded")

                htmlService
                    .login(headers, params)
                    .async()
                    .subscribe({
                        ErrorHanding.getProblemFromHtmlResponse(it).apply {
                            ToastUtil.shortShow(this)
                            Logger.d(this)
                        }
                    }, {
                        if (it is HttpException && it.code() == 302) {
                            getUserProfiler()
                        } else {
                            ToastUtil.shortShow(ErrorHanding.handleError(it))
                        }
                    })
            }
        }
    }


    /**
     * 获取用户基本信息
     */
    private fun getUserProfiler() {
        htmlService.getProfiler()
            .map {
                val profileModel = ProfileModel().apply {
                    parse(it)
                }
                AppDataBase.getDataBase().profileModelDao().saveUserProfile(profileModel)
                SPUtils.setBoolean("isLogin", true)
                return@map profileModel
            }
            .async()
            .subscribe({
                loginSuccessEvent.value = it
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    /**
     * 刷新验证码
     */
    fun refreshVerifyImg() {
        getLoginData()
    }
}
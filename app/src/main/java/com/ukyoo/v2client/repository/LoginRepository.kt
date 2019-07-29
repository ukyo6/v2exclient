package com.ukyoo.v2client.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.db.AppDataBase
import com.ukyoo.v2client.data.entity.ProfileModel
import com.ukyoo.v2client.util.*
import org.jsoup.Jsoup
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

/**
 * 登录数据源
 */
class LoginRepository @Inject constructor(@Named("cached") var htmlService: HtmlService) {

    //请求参数的key
    private var nameVal: String = ""
    private var passwordVal: String = ""
    private var verifyCodeVal: String = ""
    private var once: String = ""


    /**
     * 从登录页面获取cookies
     */
    fun getLoginData(): LiveData<Resource<String>> {
        val verifyUrl = MutableLiveData<Resource<String>>()

        //清除cookie
//        NetManager.clearCookie()

        htmlService.signin()
            .async()
            .subscribe({ content ->
                if (content?.body() == null) {
                    return@subscribe
                }
                val body = Jsoup.parse(content.body())
                val boxes = body.getElementsByClass("box")

                loopOuter@ for (el in boxes) {
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

                        verifyUrl.value =
                            Resource.success("https://www.v2ex.com" + verifyStr.substring(startIndex, endIndex))
                        break@loopOuter
                    }
                }
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })

        return verifyUrl
    }


    /**
     * 登录
     */
    fun login(username: String, password: String, verifyCode: String): MutableLiveData<Resource<ProfileModel>> {
        val result = MutableLiveData<Resource<ProfileModel>>()

        val params = HashMap<String, String>()
        params[nameVal] = username
        params["once"] = once
        params[passwordVal] = password
        params[verifyCodeVal] = verifyCode

        val headers = HashMap<String, String>()
        headers["Origin"] = "https://www.v2ex.com"
        headers["Referer"] = "https://www.v2ex.com/signin"
        headers["Content-Type"] = "application/x-www-form-urlencoded"

        htmlService
            .login(headers, params)
            .async()
            .doOnSubscribe {
                //loading status
                result.value = Resource.loading(null)
            }
            .subscribe({
                ErrorHanding.getProblemFromHtmlResponse(it).apply {
                    //error status
                    Logger.d(this)
                    result.value = Resource.error(this, null)
                }
            }, {
                if (it is HttpException && it.code() == 302) {
                    //success status
                    getUserProfiler(result)

                } else {
                    //error status
                    Logger.d(ErrorHanding.handleError(it))
                    result.value = Resource.error(ErrorHanding.handleError(it), null)
                }
            })

        return result
    }


    /**
     * 获取用户基本信息
     */
    private fun getUserProfiler(result : MutableLiveData<Resource<ProfileModel>>)  {

        htmlService.getProfiler()
            .map {
                val profileModel = ProfileModel().apply {
                    parse(it)
                }
                //存到db  子线程
                AppDataBase.getDataBase().profileModelDao().saveUserProfile(profileModel)
                return@map profileModel
            }
            .async()
            .subscribe({
                result.value = Resource.success(it)
            }, {
                result.value = Resource.error(ErrorHanding.handleError(it), null)
            })
    }
}
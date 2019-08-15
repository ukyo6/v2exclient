package com.ukyoo.v2client.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.db.AppDataBase
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.ui.login.LoginViewModel
import com.ukyoo.v2client.util.*
import org.jsoup.Jsoup
import retrofit2.HttpException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * 登录数据源
 */
@Singleton
class LoginRepository @Inject constructor(@Named("cached") var htmlService: HtmlService) {

    //请求参数的key
    private var nameVal: String = ""
    private var passwordVal: String = ""
    private var verifyCodeVal: String = ""
    private var once: String = ""


    /**
     * 从登录页面获取cookies
     */
    fun getVerifyUrl(): LiveData<Resources<String>> {
        val verifyUrl = MutableLiveData<Resources<String>>()

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
                            Resources.success("https://www.v2ex.com" + verifyStr.substring(startIndex, endIndex))
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
    fun login(param: LoginViewModel.LoginParam): LiveData<Resources<ProfileModel>> {
        val result = MutableLiveData<Resources<ProfileModel>>()

        val params = HashMap<String, String>()
        params[nameVal] = param.userName
        params["once"] = once
        params[passwordVal] = param.pwd
        params[verifyCodeVal] = param.verifyCode

        val headers = HashMap<String, String>()
        headers["Origin"] = "https://www.v2ex.com"
        headers["Referer"] = "https://www.v2ex.com/signin"
        headers["Content-Type"] = "application/x-www-form-urlencoded"

        htmlService
            .login(headers, params)
            .async()
            .doOnSubscribe {
                //loading status
                result.value = Resources.loading()
            }
            .subscribe({
                ErrorHanding.getProblemFromHtmlResponse(it).apply {
                    //error status
                    Logger.d(this)
                    result.value = Resources.error(this)
                }
            }, {
                if (it is HttpException && it.code() == 302) {  //重定向到首页 获取用户信息
                    getUserProfiler(result)
                } else {
                    //error status
                    Logger.d(ErrorHanding.handleError(it))
                    result.value = Resources.error(ErrorHanding.handleError(it))
                }
            })

        return result
    }


    /**
     * 获取用户基本信息
     */
    private fun getUserProfiler(result: MutableLiveData<Resources<ProfileModel>>) {

        htmlService.getProfiler()
            .map {
                val profileModel = parseProfilerModel(it)
                //存到db  子线程
                AppDataBase.getDataBase().profileModelDao().saveUserProfile(profileModel)
                return@map profileModel
            }
            .async()
            .subscribe({
                result.value = Resources.success(it)
            }, {
                result.value = Resources.error(ErrorHanding.handleError(it))
            })
    }


    /**
     * 解析个人信息
     */
    private fun parseProfilerModel(responseBody: String) : ProfileModel{
        val model = ProfileModel()


        val doc = Jsoup.parse(responseBody)
        val body = doc.body()
        val elements = body.getElementsByAttributeValue("id", "Rightbar")
        val found = intArrayOf(0, 0, 0, 0)
        for (el in elements) {
            if (found[0] == 1 && found[1] == 1 && found[2] == 1 && found[3] == 1)
                break
            val tdNodes = el.getElementsByTag("td")
            for (tdNode in tdNodes) {
                val content = tdNode.toString()
                if (found[0] == 0 && content.contains("a href=\"/member/")) {
                    val aNode = tdNode.getElementsByTag("a")
                    model.username = aNode.attr("href").replace("/member/", "")
                    val avatarNode = tdNode.getElementsByTag("img")
                    if (avatarNode != null) {
                        var avatarString = avatarNode.attr("src")
                        if (avatarString.startsWith("//")) {
                            avatarString = "https:$avatarString"
                        }
                        model.avatar = avatarString
                        found[0] = 1
                    }
                } else if (found[1] == 0 && content.contains("a href=\"/my/nodes\"")) {
                    //text = 20 节点收藏
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.nodes = Integer.parseInt(text)
                        found[1] = 1
                    } catch (e: Exception) {
                        model.nodes = 0
                    }

                } else if (found[2] == 0 && content.contains("a href=\"/my/topics\"")) {
                    //text = 20 主题收藏
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.topics = Integer.parseInt(text)
                        found[2] = 1
                    } catch (e: Exception) {
                        model.topics = 0
                    }

                } else if (found[3] == 0 && content.contains("a href=\"/my/following\"")) {
                    //text = 20 特别关注
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.followings = Integer.parseInt(text)
                        found[3] = 1
                    } catch (e: Exception) {
                        model.followings = 0
                    }

                }
            }
        }

        val pattern = Pattern.compile("<a href=\"/notifications\"([^>]*)>([0-9]+) 条未读提醒</a>")
        val matcher = pattern.matcher(responseBody)
        if (matcher.find()) {
            model.notifications = Integer.parseInt(matcher.group(2))
        }

        return model
    }
}
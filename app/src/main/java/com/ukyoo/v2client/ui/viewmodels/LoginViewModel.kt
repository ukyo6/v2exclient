package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.BaseViewModel
import org.jsoup.Jsoup
import javax.inject.Inject

class LoginViewModel @Inject constructor(var htmlService: HtmlService) : BaseViewModel() {
    var username = ObservableField<String>()
    var password = ObservableField<String>()
    var verifyCode = ObservableField<String>()

    var verifyImgUrl = ObservableField<String>()
    var nameVal:String =""
    var passwordVal:String =""
    var verifyCodeVal:String =""
    var once:String =""

    fun initData(){
        getLoginData()


    }

    //从首页获取登录需要的信息
    private fun getLoginData() {
        htmlService.signin()
            .async()
            .subscribe({content ->
                val body = Jsoup.parse(content)
                val boxes = body.getElementsByClass("box")

                for (el in boxes) {
                    val cell = el.getElementsByClass("cell")
                    for (c in cell) {
                        nameVal = c.getElementsByAttributeValue("type", "text").attr("name")
                        passwordVal = c.getElementsByAttributeValue("type", "password").attr("name")
                        once = c.getElementsByAttributeValue("name", "once").attr("value")
                        verifyCodeVal = c.getElementsByAttributeValue("type", "text").attr("style")
                        if (nameVal.isEmpty() || passwordVal.isEmpty()) {
                            continue
                        }

                        val verifyStr = c.getElementsByAttributeValueContaining("style", "background-image").toString()
                        val startIndex = verifyStr.indexOf("/_captcha?")
                        val endIndex = verifyStr.indexOf("\')")
                        verifyImgUrl.set("https://www.v2ex.com" + verifyStr.substring(startIndex, endIndex))
                        break
                    }
                }
            }, {
                Logger.d("error..................")
            })
    }

    /**
     * 登录
     */
    fun login(){
        val params = HashMap<String, String>()
        params.put(nameVal, username.get()?:"")
        params.put("once", once)
        params.put(passwordVal, password.get()?:"")
        params.put(verifyCodeVal, verifyCode.get()?:"")
    }
}
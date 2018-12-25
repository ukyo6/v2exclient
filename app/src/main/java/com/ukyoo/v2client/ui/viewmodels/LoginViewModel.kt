package com.ukyoo.v2client.ui.viewmodels

import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.BaseViewModel
import org.jsoup.Jsoup
import javax.inject.Inject

class LoginViewModel @Inject constructor(var htmlService: HtmlService) : BaseViewModel() {
    var username: String = ""
    var password: String = ""
    var verifyCode: String = ""

    var verifyImgUrl: String = ""

    //登录首页
    fun signin() {
        htmlService.signin()
            .async()
            .flatMap { content ->
                val body = Jsoup.parse(content)
                val boxes = body.getElementsByClass("box")
                val params = HashMap<String, String>()
                for (el in boxes) {
                    val cell = el.getElementsByClass("cell")
                    for (c in cell) {
                        val nameVal = c.getElementsByAttributeValue("type", "text").attr("name")
                        val passwordVal = c.getElementsByAttributeValue("type", "password").attr("name")
                        val once = c.getElementsByAttributeValue("name", "once").attr("value")
                        val verifyCodeVal = c.getElementsByAttributeValue("type", "text").attr("style")

                        val verifyStr = c.getElementsByAttributeValueContaining("style", "background-image").toString()
                        val startIndex = verifyStr.indexOf("url(\'")
                        val endIndex = verifyStr.indexOf("\')")
                        verifyStr.substring(startIndex,endIndex)

                        if (nameVal.isEmpty() || passwordVal.isEmpty()) {
                            continue
                        } else{
                            params.put(nameVal, username)
                            params.put("once", once)
                            params.put(passwordVal, password)
                            params.put(verifyCodeVal, verifyCode)
                            break
                        }
                    }
                }
                return@flatMap htmlService.login(params)
            }
            .subscribe({

            }, {

            })
    }
}
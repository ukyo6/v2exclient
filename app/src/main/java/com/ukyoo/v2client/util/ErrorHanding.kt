package com.ukyoo.v2client.util

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.regex.Pattern

object ErrorHanding {

    //对应HTTP的状态码
    private val WRONG_URL = 400
    private val UNAUTHORIZED = 401
    private val FORBIDDEN = 403//服务器拒绝请求
    private val NOT_FOUND = 404//找不到url
    private val REQUEST_TIMEOUT = 408
    private val INTERNAL_SERVER_ERROR = 500
    private val BAD_GATEWAY = 502
    private val SERVICE_UNAVAILABLE = 503
    private val GATEWAY_TIMEOUT = 504

    fun handleError(throwable: Throwable): String {
        throwable.printStackTrace()
        val message: String
        if (throwable is HttpException) {             //均视为网络错误
            message = "网络错误(" + throwable.code() + ")"
        } else if (throwable is JSONException || throwable is JsonParseException) {
            message = "解析失败，请检查网络"           //均视为解析错误
        } else if (throwable is SocketTimeoutException) {  //连接超时
            message = "网络连接超时"
        } else if (throwable is UnknownHostException     //网络连接失败
            || throwable is SocketException
        ) {
            message = "网络连接失败，请检查网络!"
        } else {
            message = "未知异常，请检查网络"          //未知错误
        }//        else if (throwable instanceof ServerException) {
        //            message = throwable.getMessage();
        //        }
        return message
    }

    fun getProblemFromHtmlResponse(response: String): String {
        val errorPattern = Pattern.compile("<div class=\"problem\">(.*)</div>")
        val errorMatcher = errorPattern.matcher(response)
        val errorContent: String
        errorContent = if (errorMatcher.find()) {
            errorMatcher.group(1).replace("<[^>]+>".toRegex(), "")
        } else {
            "未知错误"
        }
        return errorContent
    }
}
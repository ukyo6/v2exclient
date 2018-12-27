package com.ukyoo.v2client.util;

import com.google.gson.JsonParseException;
import org.json.JSONException;
import retrofit2.HttpException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


/**
 * desc  : 处理服务器异常
 * <p>
 * created　by hewei on 2016/10/28
 */

public class ErrorHanding {

    public ErrorHanding() {
    }

    //对应HTTP的状态码
    private static final int WRONG_URL = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;//服务器拒绝请求
    private static final int NOT_FOUND = 404;//找不到url
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static String handleError(Throwable throwable) {
        throwable.printStackTrace();
        String message;
        if (throwable instanceof HttpException) {             //均视为网络错误
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                default:
                    message = "网络错误(" + httpException.code() + ")";
                    break;
            }
        } else if (throwable instanceof JSONException
                || throwable instanceof JsonParseException) {
            message = "解析失败，请检查网络";           //均视为解析错误
        } else if (throwable instanceof SocketTimeoutException) {  //连接超时
            message = "网络连接超时";
        } else if (throwable instanceof UnknownHostException     //网络连接失败
                || throwable instanceof SocketException
                ) {
            message = "网络连接失败，请检查网络!";
        }
//        else if (throwable instanceof ServerException) {
//            message = throwable.getMessage();
//        }
        else {
            message = "未知异常，请检查网络";          //未知错误
        }
        return message;
    }
}

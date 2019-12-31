package com.ukyoo.v2client.widget.webview

import org.json.JSONObject


/**
 * JS触发的
 */

interface IBridgeHandler {
    /**
     * bridge名称
     * @return
     */
    fun getName(): Array<String>

    /**
     * Birdge触发时调用调用
     * @param jsonObject
     * @param callBackFunction
     */
    fun onHandle(
        functionName: String?,
        jsonObject: JSONObject?,
        callBackFunction: CallBackFunction?
    )

    interface CallBackFunction{}
}


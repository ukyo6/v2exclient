package com.ukyoo.v2client.widget.webview

import android.net.Uri
import android.webkit.*

/**
 * WebView触发的
 */
interface IWebviewCallback {

    fun onPageFinished()
    fun onPageStart()
    fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean

    fun shouldInterceptRequest(
        view: WebView,
        webResourceRequest: WebResourceRequest
    ): WebResourceResponse

    fun onShowFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: WebChromeClient.FileChooserParams
    ): Boolean

    fun onLoadResource(webView: WebView, url: String)


}
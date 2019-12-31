package com.ukyoo.v2client.widget.webview

import android.content.Context
import android.text.TextUtils
import android.webkit.*
import com.ukyoo.v2client.data.api.NetManager
import okhttp3.Request
import java.util.*
import kotlin.collections.HashMap

class V2WebView(context: Context) : WebView(context) {


    /**
     * 使用OKhttp进行网络请求, 这样就可以使用HttpDNS
     */
    class V2WebViewClient : WebViewClient() {
        override fun shouldInterceptRequest(
            view: WebView,
            webResourceRequest: WebResourceRequest
        ): WebResourceResponse? {
            val url = view.url

            val builder = Request.Builder()
            //构造请求
            builder.url(url).method(webResourceRequest.getMethod(), null);
            val requestHeaders = webResourceRequest.requestHeaders

            if (requestHeaders.isNotEmpty()) {
                requestHeaders.forEach() {
                    builder.addHeader(it.key, it.value)
                }
            }


            val mClient = NetManager.getHttpClient()
            val syncCall = mClient.newCall(builder.build())
            val response = syncCall.execute()


            if (response.body() != null) {
                val body = response.body()
                val map = HashMap<String, String>()
                val headers = response.headers()

                for (i in 0 until headers.size()) {
                    //相应体的header
                    map.put(headers.name(i), headers.value(i))
                }

                //MIME类型
                var mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url))
                val contentType = response.headers().get("Content-Type")
                var encoding = "utf-8"
                //获取ContentType和编码格式
                if (contentType != null && "" != contentType) {
                    if (contentType.contains(";")) {
                        val args = contentType.split(";")
                        mimeType = args[0]

                        val args2 = args[1].trim().split("=")
                        if (args2.size == 2 && args2[0].trim().toLowerCase(Locale.getDefault()).equals(
                                "charset"
                            )
                        ) {
                            encoding = args2[1].trim()
                        }
                    } else {
                        mimeType = contentType;
                    }
                }
                val webResourceResponse = WebResourceResponse(
                    mimeType,
                    encoding,
                    body?.byteStream()
                )

                var message = response.message()
                val code = response.code()
                if (TextUtils.isEmpty(message) && code == 200) {
                    message = "OK" //message不能为空followRedirects
                }

                webResourceResponse.setStatusCodeAndReasonPhrase(code, message)
                webResourceResponse.setResponseHeaders(map)

                return webResourceResponse
            }

            return super.shouldInterceptRequest(view, webResourceRequest)
        }
    }
}


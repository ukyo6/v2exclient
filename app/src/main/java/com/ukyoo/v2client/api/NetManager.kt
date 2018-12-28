package com.ukyoo.v2client.api

import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.App
import com.ukyoo.v2client.BuildConfig
import com.ukyoo.v2client.util.isNetworkConnected
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *  created by hewei
 */
object NetManager {

    const val HOST_API = "https://www.v2ex.com"

    //只在debug下输出日志的拦截器
    private val okhttpLogInterceptor = Interceptor { chain ->
        val request = chain.request()

        if (BuildConfig.DEBUG) {
            val t1 = System.nanoTime()
            val response = chain.proceed(request)
            val t2 = System.nanoTime()

            val time = (t2 - t1) / 1e6

            val contentType = response.body()!!.contentType()
            val bodyString = response.body()!!.string()

            val msg = (request.method() + "\nurl->" + request.url()
                    + "\ntime->" + time
                    + "ms\nheaders->" + request.headers()
                    + "\nresponse code->" + response.code()
                    + "\nresponse headers->" + response.headers()
                    + "\nbody->" + bodyString)

            if (request.method() == "GET") {
                Logger.i(msg)
            } else if (request.method() == "POST") {
                val copyRequest = request.newBuilder().build()
                //                            if (copyRequest.body() instanceof FormBody) {
                val buffer = Buffer()
                copyRequest.body()!!.writeTo(buffer)
                Logger.i("request params:" + buffer.readUtf8())
                //                        }
                Logger.i(msg)
            } else if (request.method() == "PUT") {
                Logger.i(msg)
            } else if (request.method() == "DELETE") {
                Logger.i(msg)
            }
            val body = ResponseBody.create(contentType, bodyString)
            response.newBuilder().body(body).build()
        } else {
            chain.proceed(request)
        }
    }
    //设置缓存拦截器
    private val cacheInterceptor = Interceptor { chain ->
        var request = chain.request()
        if (!isNetworkConnected()) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }

        val response = chain.proceed(request)

        if (isNetworkConnected()) {
            val maxAge = 0 * 60 // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build()
        } else {
            val maxStale = 0 // 无网络时，设置超时为3秒
            response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        response
    }

    /**
     * jsoup解析返回的html
     */
    fun getHtmlClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HOST_API)
            .client(getHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    /**
     * gson解析返回的json
     */
    fun getJsonClient():Retrofit{
        return Retrofit.Builder()
            .baseUrl(HOST_API)
            .client(getHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    /**
     * jsoup解析返回的html
     * 带cookie
     */
    fun getHtmlClient2():Retrofit{
        return Retrofit.Builder()
            .baseUrl(HOST_API)
            .client(getHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getHttpClient2():OkHttpClient{
        return OkHttpClient.Builder()
            .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            .followSslRedirects(false)
            .cookieJar(LocalCookieJar())   //为OkHttp设置自动携带Cookie的功能
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(okhttpLogInterceptor)
            .build()
    }

    private fun getHttpClient(): OkHttpClient {
        val httpCacheDirectory = File(App.instance().cacheDir, "responses") //缓存位置大小
        val cache = Cache(httpCacheDirectory, (50 * 1024 * 1024).toLong())

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(okhttpLogInterceptor)
            .cache(cache)
            .build()
    }


    //CookieJar是用于保存Cookie的
    internal class LocalCookieJar : CookieJar {
        var cookies: List<Cookie>? = null
        override fun loadForRequest(arg0: HttpUrl): List<Cookie> {
            return if (cookies != null) cookies!! else ArrayList()
        }

        override fun saveFromResponse(arg0: HttpUrl, cookies: List<Cookie>) {
            this.cookies = cookies
        }
    }


}
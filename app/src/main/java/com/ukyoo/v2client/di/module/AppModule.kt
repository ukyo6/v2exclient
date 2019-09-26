package com.ukyoo.v2client.di.module

import android.app.Application
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.data.AppDataBase
import com.ukyoo.v2client.data.dao.NodeModelDao
import com.ukyoo.v2client.util.CONSTANT
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * 页面描述：AppModule
 *
 * Created by hewei
 */
@Module
class AppModule(val app:Application){

    //提供单例的Application
    @Singleton
    @Provides
    fun provideApp() = app

    //提供解析html的client
    @Singleton
    @Provides
    @Named(CONSTANT.HTMLPARSE)
    fun provideHtmlClient(): Retrofit = NetManager.getHtmlClient()

    //提供解析json的client
    @Singleton
    @Provides
    @Named(CONSTANT.JSONPARSE)
    fun provideJsonClient(): Retrofit = NetManager.getJsonClient()

    //html API
    @Singleton
    @Provides
    @Named("non_cached")
    fun privideHtmlService():HtmlService = provideHtmlClient().create(HtmlService::class.java)

    //json API
    @Singleton
    @Provides
    fun provideJsonService():JsonService = provideJsonClient().create(JsonService::class.java)

    //html API with cookie
    @Singleton
    @Provides
    @Named("cached")
    fun provideHtmlCookieService():HtmlService = NetManager.getHtmlClient2().create(HtmlService::class.java)

    @Singleton
    @Provides
    fun provideRoomDataBase():AppDataBase = AppDataBase.getDatabase()

}
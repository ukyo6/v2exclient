package com.ukyoo.v2client.di.module

import android.app.Application
import androidx.room.RoomDatabase
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.api.NetManager
import com.ukyoo.v2client.db.AppDataBase
import com.ukyoo.v2client.db.NodeModelDao
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

    //提供单例的htmlService
    @Singleton
    @Provides
    fun privateHtmlService():HtmlService = provideHtmlClient().create(HtmlService::class.java)

    //提供单例的jsonService
    @Singleton
    @Provides
    fun provideJsonService():JsonService = provideJsonClient().create(JsonService::class.java)

    @Singleton
    @Provides
    fun provideRoomDataBase():NodeModelDao = AppDataBase.getDataBase().nodeModelDao()

}
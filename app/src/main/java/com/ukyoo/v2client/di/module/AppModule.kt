package com.ukyoo.v2client.di.module

import android.app.Application
import com.ukyoo.v2client.api.ApiService
import com.ukyoo.v2client.api.NetManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
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


    //提供单例的Retrofit
    @Singleton
    @Provides
    fun provideRemoteClient(): Retrofit = NetManager.getClient()

    //提供单例的ApiService
    @Singleton
    @Provides
    fun privateApiService():ApiService = provideRemoteClient().create(ApiService::class.java)


}
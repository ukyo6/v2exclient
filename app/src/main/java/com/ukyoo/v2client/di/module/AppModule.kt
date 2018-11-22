package com.ukyoo.v2client.di.module

import android.app.Application
import com.ukyoo.v2client.api.NetManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 页面描述：AppModule
 *
 * Created by ditclear on 2017/9/26.
 */
@Module
class AppModule(val app:Application){

    //提供全局的App实例
    @Singleton
    @Provides
    fun provideApp() = app

//    @Singleton
//    @Provides
//    fun provideRemoteClient(): Retrofit = NetMgr.getRetrofit(NetManager.HOST_API)
//
//    @Singleton
//    @Provides
//    fun providePaoService(): PaoService =provideRemoteClient().create(PaoService::class.java)
//
//    @Singleton
//    @Provides
//    fun provideUserService(retrofit: Retrofit) :UserService =retrofit.create(UserService::class.java)
//
//    @Singleton
//    @Provides
//    fun provideArticleDao(context:Application):ArticleDao = AppDatabase.getInstance(context).articleDao()
//
//    @Singleton
//    @Provides
//    fun provideUserDao(context:Application):UserDao = AppDatabase.getInstance(context).userDao()

}
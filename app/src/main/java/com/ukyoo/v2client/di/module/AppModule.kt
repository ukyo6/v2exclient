package com.ukyoo.v2client.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * 页面描述：AppModule
 *
 * Created by ditclear on 2017/9/26.
 */
@Module
class AppModule(val app:Application){

    @Provides @Singleton
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
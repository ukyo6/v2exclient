package com.ukyoo.v2client.di.component

import android.app.Application
import com.ukyoo.v2client.di.module.ActivityModule
import com.ukyoo.v2client.di.module.AppModule
import com.ukyoo.v2client.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

/**
 * 页面描述：AppComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@Singleton
@Component(modules = [(AppModule::class), (ViewModelModule::class)])
interface AppComponent{

    fun inject(app: Application)

    fun plus(module: ActivityModule): ActivityComponent
}
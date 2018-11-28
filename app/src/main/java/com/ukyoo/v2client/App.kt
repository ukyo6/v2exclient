package com.ukyoo.v2client

import android.app.Application
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.di.component.AppComponent
import com.ukyoo.v2client.di.component.DaggerAppComponent
import com.ukyoo.v2client.di.module.AppModule
import java.lang.IllegalStateException
import java.nio.channels.IllegalChannelGroupException

class App: Application() {

    lateinit var component: AppComponent
        private set

    companion object {
        private var instance: Application? = null
        fun instance() = instance?:throw IllegalStateException("App instance not init yet")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)

        //初始化log打印
        Logger.init().hideThreadInfo().logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)
    }
}
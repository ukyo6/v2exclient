package com.ukyoo.v2client

import android.app.Application
import com.ukyoo.v2client.di.component.AppComponent
import com.ukyoo.v2client.di.component.DaggerAppComponent
import com.ukyoo.v2client.di.module.AppModule

class App: Application() {

    lateinit var component: AppComponent
        private set

    companion object {
        private var instance: Application? = null
        fun instance() = instance?:throw Throwable("instance 还未初始化")
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        component.inject(this)
    }
}
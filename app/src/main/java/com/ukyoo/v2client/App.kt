package com.ukyoo.v2client

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.squareup.leakcanary.LeakCanary
import com.ukyoo.v2client.di.component.AppComponent
import com.ukyoo.v2client.di.component.DaggerAppComponent
import com.ukyoo.v2client.di.module.AppModule
import com.ukyoo.v2client.util.SPUtils

class App: Application() {

    lateinit var component: AppComponent
        private set

    companion object {
        var SCREEN_WIDTH = -1
        var SCREEN_HEIGHT = -1
        var DIMEN_RATE = -1.0f
        var DIMEN_DPI = -1


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

        getScreenSize()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
//        LeakCanary.install(this)

        SPUtils.init(instance())
    }

    private fun getScreenSize() {
        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(dm)
        DIMEN_RATE = dm.density / 1.0f
        DIMEN_DPI = dm.densityDpi
        SCREEN_WIDTH = dm.widthPixels
        SCREEN_HEIGHT = dm.heightPixels
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            val t = SCREEN_HEIGHT
            SCREEN_HEIGHT = SCREEN_WIDTH
            SCREEN_WIDTH = t
        }
    }
}
package com.ukyoo.v2client.base.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.orhanobut.logger.Logger

/**
 * java8非注解的方式
 */
open class IViewModel : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Logger.d("onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Logger.d("onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Logger.d("onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Logger.d("onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Logger.d("onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Logger.d("onDestroy")
    }
}
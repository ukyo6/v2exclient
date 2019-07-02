package com.ukyoo.v2client.base.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger

/**
 * Lifecycle java8非注解的方式
 */
open class LifecycleViewModel : ViewModel(), DefaultLifecycleObserver {

    var lifecycleOwner: LifecycleOwner? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifecycleOwner = owner
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
//        lifecycleOwner = null
    }
}
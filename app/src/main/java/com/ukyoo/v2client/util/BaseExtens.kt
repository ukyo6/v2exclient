package com.ukyoo.v2client.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.FlowableSubscribeProxy
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.ukyoo.v2client.App
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit




fun <T> Flowable<T>.async(withDelay: Long = 0): Flowable<T> =
    this.subscribeOn(Schedulers.io()).delay(withDelay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.async(withDelay: Long = 0): Single<T> =
    this.subscribeOn(Schedulers.io()).delay(withDelay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.bindLifeCycle(owner: LifecycleOwner): SingleSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun <T> Flowable<T>.bindLifeCycle(owner: LifecycleOwner): FlowableSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun <T> T.apply(f: T.() -> Unit): T { f(); return this }



fun isNetworkConnected(): Boolean {
    val connectivityManager =
        App.instance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null
}



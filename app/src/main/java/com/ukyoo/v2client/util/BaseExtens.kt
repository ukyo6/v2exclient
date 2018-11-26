package com.ukyoo.v2client.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.ukyoo.v2client.BuildConfig
import com.ukyoo.v2client.util.annotations.ToastType
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import android.content.Context.CONNECTIVITY_SERVICE
import com.ukyoo.v2client.App
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager



fun Activity.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT, @ToastType type: Int = ToastType.NORMAL) {
    when (type) {
        ToastType.WARNING -> Toast.makeText(this, msg, duration).show()
        ToastType.ERROR -> Toast.makeText(this, msg, duration).show()
        ToastType.NORMAL -> Toast.makeText(this, msg, duration).show()
        ToastType.SUCCESS -> Toast.makeText(this, msg, duration).show()
    }
}

fun <T> Flowable<T>.async(withDelay: Long = 0): Flowable<T> =
    this.subscribeOn(Schedulers.io()).delay(withDelay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.async(withDelay: Long = 0): Single<T> =
    this.subscribeOn(Schedulers.io()).delay(withDelay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.bindLifeCycle(owner: LifecycleOwner): SingleSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun <T> T.apply(f: T.() -> Unit): T { f(); return this }

fun Activity.dispatchFailure(error: Throwable?) {
    error?.let {
        if (BuildConfig.DEBUG) {
            it.printStackTrace()
        }
        if (error is SocketTimeoutException) {
            it.message?.let { toast("网络连接超时", ToastType.ERROR) }

        } else if (it is UnknownHostException || it is ConnectException) {
            //网络未连接
            it.message?.let { toast("网络未连接", ToastType.ERROR) }

        } else {
            it.message?.let { toast(it, ToastType.ERROR) }
        }
    }
}

fun isNetworkConnected(): Boolean {
    val connectivityManager =
        App.instance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null
}
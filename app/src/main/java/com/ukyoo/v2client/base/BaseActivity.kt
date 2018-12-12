package com.ukyoo.v2client.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ukyoo.v2client.App
import com.ukyoo.v2client.di.component.ActivityComponent
import com.ukyoo.v2client.di.module.ActivityModule
import com.ukyoo.v2client.util.annotations.ToastType
import com.ukyoo.v2client.util.dispatchFailure
import com.ukyoo.v2client.util.toast
import javax.inject.Inject


/**
 * 页面描述：
 *
 * Created by ditclear on 2017/9/27.
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), Presenter {

    protected lateinit var mBinding: VB

    protected lateinit var mContext: Context

    private var activityComponent: ActivityComponent? = null

    protected var autoRefresh = true
    protected var delayToTransition = false

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @NonNull
    fun getComponent(): ActivityComponent {
        if (activityComponent == null) {
            val mainApplication = application as App
            activityComponent = mainApplication.component.plus(ActivityModule(this))
        }
        return activityComponent as ActivityComponent
    }

    inline fun <reified T : ViewModel> getInjectViewModel() = ViewModelProviders.of(this, factory).get(T::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<VB>(this, getLayoutId())
        mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        mBinding.setLifecycleOwner(this)
        mContext = this

        restoreArgs(savedInstanceState)
        initView()
        if (delayToTransition) {
            afterEnterTransition()
        } else if (autoRefresh) {
            loadData(true)
        }
    }

    /**
     * 恢复数据的
     */
    open fun restoreArgs(savedInstanceState: Bundle?) {
    }

    /**
     * 保存数据的
     */
    open fun saveArgs(outState: Bundle?){
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        saveArgs(outState)
        super.onSaveInstanceState(outState)
    }

//    private val enterTransitionListener by lazy {
//        @TargetApi(Build.VERSION_CODES.O)
//        object : TransitionListenerAdapter() {
//            override fun onTransitionResume(transition: android.transition.Transition?) {
//
//            }
//
//            override fun onTransitionPause(transition: android.transition.Transition?) {
//            }
//
//            override fun onTransitionCancel(transition: android.transition.Transition?) {
//            }
//
//            override fun onTransitionStart(transition: android.transition.Transition?) {
//            }
//
//            override fun onTransitionEnd(transition: android.transition.Transition?) {
//                loadData(true)
//            }
//        }
//    }

    private fun afterEnterTransition() {
//        window.enterTransition.addListener(enterTransitionListener)
    }

    override fun onDestroy() {
        super.onDestroy()
//        window.enterTransition.removeListener(enterTransitionListener)
    }

    abstract override fun loadData(isRefresh: Boolean)

    abstract fun initView()

    abstract fun getLayoutId(): Int


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initBackToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        val bar = supportActionBar
        if (bar != null) {
            bar.title = null
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setDisplayShowHomeEnabled(true)
            bar.setDisplayShowTitleEnabled(true)
            bar.setHomeButtonEnabled(true)
        }
    }

    fun toastSuccess(msg: String?) {
        msg?.let { toast(it, ToastType.SUCCESS) }
    }

    fun toastFailure(error: Throwable?) {
        dispatchFailure(error)
    }

    protected fun <T> autoWired(key: String, default: T? = null): T? {
        return intent?.extras?.let { findWired(it, key, default) }
    }

    private fun <T> findWired(bundle: Bundle, key: String, default: T? = null): T? {
        return if (bundle.get(key) != null) {
            try {
                bundle.get(key) as T
            } catch (e: ClassCastException) {
                e.printStackTrace()
                null
            }
        } else default

    }
}
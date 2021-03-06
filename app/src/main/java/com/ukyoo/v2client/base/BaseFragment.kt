package com.ukyoo.v2client.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.di.component.FragmentComponent
import com.ukyoo.v2client.di.module.FragmentModule
import javax.inject.Inject



abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    protected lateinit var mBinding: VB

    protected lateinit var mContext: Context

    protected var lazyLoad = false

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private var isPrepared: Boolean = false
    //标志当前页面是否可见
    private var visible: Boolean = false
    //是否已经加载过
    private var isLoaded: Boolean = false

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var fragmentComponent: FragmentComponent? = null
    private lateinit var fragmentComponentBuilder: FragmentComponent.Builder

    inline fun <reified T : ViewModel> getInjectViewModel(): T =
        ViewModelProviders.of(this, factory).get(T::class.java)

    @NonNull
    fun getComponent(): FragmentComponent {
        if (fragmentComponent != null) {
            return fragmentComponent as FragmentComponent
        }

        val activity = activity
        if (activity is BaseActivity<*>) {
            fragmentComponentBuilder = activity.getComponent().supplyFragmentComponentBuilder()
            fragmentComponent = fragmentComponentBuilder.fragmentModule(FragmentModule(this)).build()
            return fragmentComponent as FragmentComponent
        } else {
            throw IllegalStateException(
                "The activity of this fragment is not an instance of BaseActivity"
            )
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false)
        mBinding.setLifecycleOwner(this)
        mBinding.executePendingBindings()
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity ?: throw Exception("activity 为null")

        isPrepared = true
        if (isLazyLoad()) {
            lazyLoad()
        } else {
            loadData(true)//数据请求
        }
    }

    /**
     * 是否可见，延迟加载
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            visible = true
            onVisible()
        } else {
            visible = false
            onInvisible()
        }
    }

    open fun onInvisible() {

    }

    protected open fun onVisible() {
        lazyLoad()
    }


    private fun lazyLoad() {
        if (!visible || !isPrepared) {
            return
        }

        if (!isLoaded) {
            loadData(true)//数据请求
            isLoaded = true
        }
    }

    abstract fun loadData(isRefresh: Boolean)

    abstract fun getLayoutId(): Int

    abstract fun isLazyLoad(): Boolean
}
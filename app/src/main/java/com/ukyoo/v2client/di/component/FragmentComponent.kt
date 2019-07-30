package com.ukyoo.v2client.di.component

import com.ukyoo.v2client.di.module.FragmentModule
import com.ukyoo.v2client.di.scope.FragmentScope
import com.ukyoo.v2client.ui.main.*
import com.ukyoo.v2client.ui.userinfo.RecentRepliesFragment
import com.ukyoo.v2client.ui.userinfo.RecentTopicsFragment
import dagger.Subcomponent

/**
 * 页面描述：FragmentComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NodesFragment)
    fun inject(fragment: ProfilerFragment)
    fun inject(fragment: TopicsFragment)
    fun inject(fragment: RecentRepliesFragment)
    fun inject(fragment: RecentTopicsFragment)

    @Subcomponent.Builder
    interface Builder {
        fun fragmentModule(module: FragmentModule): Builder

        fun build(): FragmentComponent
    }
}